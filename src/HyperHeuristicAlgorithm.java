import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import AbstractClasses.ProblemDomain.HeuristicType;

/*
 * Class extending the HyFlex's HyperHeuristic class 
 */
public class HyperHeuristicAlgorithm extends HyperHeuristic{
	
	private ArrayList<Double> solution = new ArrayList<Double>();
	// 
	private ArrayList<Integer> crossoverHeuristic = new ArrayList<Integer>();
	private ArrayList<Integer> localSearchHeuristic = new ArrayList<Integer>();
	private ArrayList<Integer> ruinRecreateHeuristic = new ArrayList<Integer>();
	private ArrayList<Integer> mutationHeuristic = new ArrayList<Integer>();
	private ArrayList<Integer> perturbationHeuristic = new ArrayList<Integer>();
	//
	
	private HashMap<Integer, HeuristicType> heuristicTypes = new HashMap<Integer,HeuristicType>();

	private final int populationSize = 200;
	
	public static ProblemDomain problem;
	
	public HyperHeuristicAlgorithm(long seedValue) {
		// TODO Auto-generated constructor stub
		super(seedValue);
	}
	
	int getPopulationSize()
	{
		return populationSize;
	}
	
	public void intializeSolutions(ProblemDomain problem)
	{
		for(int i= 0;i<(populationSize+2);i++)
		{
			problem.initialiseSolution(i);
			solution.add(Double.MAX_VALUE);
		}
	}
	/*
	 * Need to implement the actual binary Tournament algorithm
	 */
	private int binaryTournament(ArrayList<Double> solutions)
	{
		return (int) (Math.random()*populationSize);
	}
	
	public void intializeLowLevelHueristicIndexes()
	{
		for(int heuristic : problem.getHeuristicsOfType(HeuristicType.MUTATION))
		{
			heuristicTypes.put(heuristic, HeuristicType.MUTATION);		
			mutationHeuristic.add(heuristic);
			perturbationHeuristic.add(heuristic);
		}
		
		for(int heuristic : problem.getHeuristicsOfType(HeuristicType.CROSSOVER))
		{
			heuristicTypes.put(heuristic, HeuristicType.CROSSOVER);
			crossoverHeuristic.add(heuristic);
			perturbationHeuristic.add(heuristic);
		}
		
		for(int heuristic : problem.getHeuristicsOfType(HeuristicType.RUIN_RECREATE))
		{
			heuristicTypes.put(heuristic, HeuristicType.RUIN_RECREATE);
			ruinRecreateHeuristic.add(heuristic);
			perturbationHeuristic.add(heuristic);
		}
		
		for(int heuristic : problem.getHeuristicsOfType(HeuristicType.LOCAL_SEARCH))
		{
			heuristicTypes.put(heuristic, HeuristicType.LOCAL_SEARCH);
			localSearchHeuristic.add(heuristic);
		}
	
	}
	/*
	 * function retrieving the random cross over heuristics
	 */
	private int getRandomCrossoverHeuristic()
	{
		int crossoverHeuristicCount = crossoverHeuristic.size();
		return (int)(Math.random()*crossoverHeuristicCount-1);
	}
	
	/*
	 * function retrieving the random Mutation heuristics
	 */
	private int getMutationHeuristic()
	{
		int mutationHeuristicCount = mutationHeuristic.size();
		return (int)(Math.random()*mutationHeuristicCount-1);
	} 
	
	/*
	 * function retrieving the Local Search Heuristics
	 */
	private int getLocalSearchHeuristic()
	{
		int localSearchHeuristicCount = localSearchHeuristic.size();
		return (int)(Math.random()*localSearchHeuristicCount-1);
	}
	
	/*
	 * function retrieving the Ruin Recreate Heuristics
	 */
	private int getRuinRecreateHeuristic()
	{
		int ruinRecreateHeuristicCount = ruinRecreateHeuristic.size();
		return (int)(Math.random()*ruinRecreateHeuristicCount-1);
	}
	
	/*
	 * @description
	 * Function which initializes the Heuristic Parameters
	 */
	public LinkedList<HeuristicParameters> createIntializeHeurisitcParameters()
	{
		//LinkedList to hold a list of Heuristic Patterns
		LinkedList<HeuristicParameters> heuristicTypesParameters = new LinkedList<HeuristicParameters>();
		int heuristicTypesPopulation = heuristicTypes.size();
		
		for(int i=0; i<heuristicTypesPopulation;i++)
		{
			HeuristicParameters heuristicParameters = new HeuristicParameters();
			
			//Perturbation Phase includes Crossover, Local Search and Mutation heuristics
			if(perturbationHeuristic.size() > 0)
			{
				heuristicParameters.perturbationPhase.add(perturbationHeuristic.get(i%perturbationHeuristic.size()));
				heuristicParameters.mutationIntensity.add(rng.nextDouble());
				int additionalPertubationOperator = perturbationHeuristic.get(rng.nextInt(perturbationHeuristic.size()));
				int j=0;
				
				while(j < 5 && (heuristicTypes.get(additionalPertubationOperator)==heuristicTypes.get(heuristicParameters.perturbationPhase.get(0)))){
					additionalPertubationOperator = perturbationHeuristic.get(rng.nextInt(perturbationHeuristic.size()));
					j++;
				}
			
				if(heuristicTypes.get(heuristicParameters.perturbationPhase.get(0))==HeuristicType.CROSSOVER)
				{
					heuristicParameters.perturbationPhase.add(additionalPertubationOperator);
					heuristicParameters.mutationIntensity.add(rng.nextDouble());
				}else{
					heuristicParameters.perturbationPhase.add(0,additionalPertubationOperator);
					heuristicParameters.mutationIntensity.add(0,rng.nextDouble());
				}
			}
			//Initializing parameters for Local Search
			heuristicParameters.localSearchPhase.addAll(localSearchHeuristic);
			Collections.shuffle(heuristicParameters.localSearchPhase);
			//
			for (int j=0; j<heuristicParameters.localSearchPhase.size(); j++) {
				heuristicParameters.depthOfSearch.add(rng.nextDouble());
			}
			heuristicTypesParameters.add(heuristicParameters);
		}
		
		return heuristicTypesParameters;
	}

	/*
	 * @parameters: 
	 * heuristicTypesParameters: Existing Heuristic Type Parameter
	 * 
	 * @description
	 * The function takes the existing Heuristic Type Parameters. 
	 * Mutates each parameter to result Heuristic Pattern and it is 
	 * added to the linkedlist at the end
	 */
	public void mutateHeuristicParameters(LinkedList<HeuristicParameters> heuristicTypesParameters)
	{
		int heuristicTypesParametersSize = heuristicTypesParameters.size();	
		for(int i=0; i < heuristicTypesParametersSize;i++)
		{
			
			HeuristicParameters newHeuristicParameter = new HeuristicParameters(heuristicTypesParameters.get(i));
			double randomValue = rng.nextDouble();
			if(randomValue < 0.5)
			{
				//Mutate Perturbation based heuristics
				int perturbationPhaseSize = newHeuristicParameter.perturbationPhase.size();

				for(int j=0; j < perturbationPhaseSize;j++)
				{
					newHeuristicParameter.mutationIntensity.set(j, rng.nextDouble());
				}
			}else{//Mutate Local Search based heuristics.
				
				int localSearchPhaseSize = newHeuristicParameter.localSearchPhase.size();
				for(int j=0; j < localSearchPhaseSize;j++)
				{
					newHeuristicParameter.depthOfSearch.set(j, rng.nextDouble());
				}
			}	
			heuristicTypesParameters.add(newHeuristicParameter);
		}
		
	}
	
	/*
	 * @parameter
	 * index1
	 * index2
	 * resultantIndex: Indexing holding the offspring
	 * 
	 * @description
	 * the function updates the offspring with the worst
	 * of the parents.
	 */
	
	public void updateSolutionSpace(int index1, int index2, int resultantIndex)
	{
		if(problem.getFunctionValue(index1) > problem.getFunctionValue(index2))
		{
			if(problem.getFunctionValue(index1) > problem.getFunctionValue(resultantIndex))
			{
				problem.copySolution(resultantIndex, index1);
			}else{
				problem.copySolution(index1,resultantIndex);
			}
		}else{
			if(problem.getFunctionValue(index2) > problem.getFunctionValue(resultantIndex))
			{
				problem.copySolution(resultantIndex,index2 );
			}else{
				problem.copySolution(index2, resultantIndex);
			}
		}
	}
	/*
	 * Algorithm implementing the HyperHeuristic algorithm
	 */
	public void run(int iterations)
	{	
		LinkedList<HeuristicParameters> heuristicTypesParameters = createIntializeHeurisitcParameters();
		for(int i=0; i < iterations;i++)
		{
			
			mutateHeuristicParameters(heuristicTypesParameters);			
			LinkedList<HeuristicParameters> binaryTournamentWinningHeuristicParameters = new LinkedList<HeuristicParameters>();
			while(heuristicTypesParameters.size() >=2)
			{			
				HeuristicParameters heuristicPattern1 = heuristicTypesParameters.remove();
				HeuristicParameters heuristicPattern2 = heuristicTypesParameters.remove();
				int index1 = binaryTournament(solution);
				int index2 = binaryTournament(solution);
				
				int resultantIndex1 = (int)heuristicPattern1.applyPattern(problem,populationSize,heuristicTypes,index1,index2,populationSize);
				int resultantIndex2 = (int)heuristicPattern2.applyPattern(problem,populationSize,heuristicTypes,index1,index2,populationSize+1);
				
				if(problem.getFunctionValue(resultantIndex1) < problem.getFunctionValue(resultantIndex2))
				{
					//heuristicPattern1 won over heuristicPattern2
					updateSolutionSpace(index1, index2, resultantIndex1);
					binaryTournamentWinningHeuristicParameters.add(heuristicPattern1);
				}else{
					//heuristicPattern2 won over heuristicPattern1
					updateSolutionSpace(index1, index2, resultantIndex2);
					binaryTournamentWinningHeuristicParameters.add(heuristicPattern2);
				}
				System.out.println(problem.getBestSolutionValue());
//				System.out.println(problem.getFunctionValue(resultantIndex1)+" "+ problem.getFunctionValue(resultantIndex1)+" "+problem.getBestSolutionValue());
				//
			}
			//Adding the winners back to the heuristic Types parameters
			for(HeuristicParameters heuristicParameters : binaryTournamentWinningHeuristicParameters){
				heuristicTypesParameters.add(heuristicParameters);
			}
		}
		//
		
		System.out.println(problem.bestSolutionToString());
}
	@Override
	protected void solve(ProblemDomain arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
