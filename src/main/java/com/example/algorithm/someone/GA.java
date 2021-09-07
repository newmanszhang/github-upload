package com.example.algorithm.someone;

import com.example.algorithm.Common.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GA {
    CommonUtils cus;
    private List<List<Integer>> first_population;  // 初始种群
    private List<List<Integer>> next_population;  // 下一代种群
    private Integer up_gene;// 基因位取值范围上限(如果是0、1，则上限为2）
    private Integer population_size;
    private Integer gene_length;
    private List<Integer> best_chrom;  // 最优个体
    private double best_fitness;  // 最优适应值

    //GA算法参数
    private int number_iteration;  // 迭代次数
    private double pb_mutation;  // 变异算子
    private double pb_crossover;  // 交叉算子
    private double pb_select;  // 选择概率

    //GA算法主流程
    public void GA(){
        //参数初始化
        this.pb_select=0.8;
        this.pb_crossover=0.9;
        this.pb_mutation=0.9;
        this.gene_length=20;
        this.population_size=100;
        this.up_gene=2;
        this.number_iteration=100;

        this.first_population=new ArrayList<>();
        this.next_population=new ArrayList<>();
        this.cus = new CommonUtils();

        initPopulation();
        for (int i = 0; i < this.number_iteration; i++) {
//            this.current_iteration = i;
            select();
            crossOver();
            mutation();
        }
        System.out.print("最优适应值: " + this.best_fitness);
        System.out.print(" 最优个体: ");
        for (int j = 0; j < this.best_chrom.size(); j++) {
            if (j != 0) {
                System.out.print(",");
            }
            System.out.print(+this.best_chrom.get(j));
        }
    }
    // 1. 种群初始化
    private void initPopulation() {
        Random random = new Random();
        int m = this.population_size;
        int n = this.gene_length;
        for (int i = 0; i < m; i++) {
            List<Integer> chromosome = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                chromosome.add(j, random.nextInt(this.up_gene));
            }
            this.first_population.add(chromosome);
            this.next_population.add(chromosome);
        }
    }

    //2.选择(轮盘赌)
    private void select(){
        List<List<Integer>> copyPopulation = this.next_population;

        // 计算种群适应度
        List<Double> populationFitness = this.calFitness(first_population);
        // 更新最优染色体
        this.updateBestChoromosome(populationFitness);

        // 根据适应度计算每个染色体被选中概率
        List<Double> probabilities = new ArrayList<>();
        double sumFitness = 0;
        for (double pf : populationFitness) sumFitness += pf;
        for (int i = 0; i < population_size; i++) probabilities.add(populationFitness.get(i) / sumFitness);

        // 计算每个染色体被选中的累计概率
        List<Double> cumProbabilities = new ArrayList<>();
        double cumProbability = 0; // cum from accumulate
        for (double pb : probabilities) {
            cumProbability += pb;
            cumProbabilities.add(cumProbability);
        }
        // 选择
        Random random = new Random();
        this.next_population.set(0, new ArrayList<>(this.best_chrom));
        for (int i = 1; i <population_size ; i++) {
            // 生成[0,1)之间的随机数
            double rand = random.nextDouble();
            int idx = this.cus.lowerBoundSearch(cumProbabilities, rand);
            if (idx == -1) {
                this.next_population.set(i, new ArrayList<>(copyPopulation.get(i)));
                this.first_population.set(i, new ArrayList<>(copyPopulation.get(i)));
            } else {
                this.next_population.set(i, new ArrayList<>(copyPopulation.get(idx)));
                this.first_population.set(i, new ArrayList<>(copyPopulation.get(idx)));
            }
        }
    }

    // 3. 交叉
    private void crossOver() {
        List<List<Integer>> copyPopulation = new ArrayList<>(this.next_population);
        int m = this.population_size;
        int n = this.gene_length;
        double pbCrossover = this.pb_crossover;

        // 产生交叉染色体数量，且确保个数是偶数
        int numCrossover = (int) (m * pbCrossover);
        if ((numCrossover >> 1) != 0) numCrossover++;

        // 生成不同的随机数
        CommonUtils commonUtils = new CommonUtils();
        List<Integer> crossoverIndexes = commonUtils.generateDifferentRandomNumbers(m, numCrossover);

        // 调试用
//        System.out.println("交叉操作生成的随机数组： \n" + crossoverIndexes);

        // 对于randIndexes中不存在的元素，不用交叉，直接复制
        for (int i = 0; i < m; i++) {
            if (crossoverIndexes.contains(i)) continue;
            this.next_population.set(i, copyPopulation.get(i));
        }

        // crossover
        Random random = new Random();
        for (int i = 0; i < numCrossover - 1; i++) {
            int a = crossoverIndexes.get(i);
            int b = crossoverIndexes.get(i + 1);
            // 随机产生一个交叉点
            int crossoverPoint = random.nextInt(n);
            for (int j = 0; j < n; j++) {
                if (j < crossoverPoint) {
                    this.next_population.get(a).set(j, copyPopulation.get(a).get(j));
                    this.next_population.get(b).set(j, copyPopulation.get(b).get(j));
                } else {
                    this.next_population.get(a).set(j, copyPopulation.get(b).get(j));
                    this.next_population.get(b).set(j, copyPopulation.get(a).get(j));
                }
            }
        }

    }


    // 4. 变异
    private void mutation() {
        int m = this.population_size;
        int n = this.gene_length;
        double pbMutation = this.pb_mutation;
        // 计算需要变异的基因个数
        int geneNum = (int) (m * n * pbMutation);
        // 将所有基因按照十进制编码，随机抽取gene_Num个基因
        List<Integer> mutationIndexes = this.cus.generateDifferentRandomNumbers(m * n, geneNum);
        // 确定需变异基因位置
        Random random = new Random();
        for (int i = 0; i < geneNum; i++) {
            int chromIndex = mutationIndexes.get(i) / n;
            int geneIndex = mutationIndexes.get(i) % n;
            // mutate
            this.next_population.get(chromIndex).set(geneIndex, random.nextInt(this.up_gene));
        }

    }

    //计算适应值函数，可根据实际情况重写
    List<Double> calFitness(List<List<Integer>> population) {
        List<Double> populationFitness = new ArrayList<>();
        for (int i = 0; i < population.size(); i++) {
            List<Integer> chromosome=population.get(i);
            Integer size = chromosome.size();
            Double sum = 0.0;
            for (int j = 0; j < size; j++) {
                sum += chromosome.get(j);
            }
            populationFitness.add(sum);
        }
        return populationFitness;
    }

    // 更新种群最优染色体
    private void updateBestChoromosome(List<Double> populationFitness) {
        for (int i = 0; i < populationFitness.size(); i++) {
            double f = populationFitness.get(i);
            if (f > this.best_fitness) {
                this.best_chrom = new ArrayList<>(next_population.get(i));
                this.best_fitness = f;
            }
        }
    }
}
