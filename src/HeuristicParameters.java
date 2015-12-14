import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.text.DecimalFormat;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import AbstractClasses.ProblemDomain.HeuristicType;

/*
 * Class representing the Hyper-Heuristic Search Pattern
 */
public class HeuristicParameters{
	
	//List of Hyper Hueristic Parameters
	ArrayList<Integer> perturbationPhase;
	ArrayList<Double> mutationIntensity;
	ArrayList<Integer> localSearchPhase;
	ArrayList<Double> depthOfSearch;
	//
	
	//Copy Constructor
	@SuppressWarnings("unchecked")
	public HeuristicParameters(HeuristicParameters copy)
	{
		this.perturbationPhase = (ArrayList<Integer>) copy.perturbationPhase.clone();
		this.mutationIntensity = (ArrayList<Double>) copy.mutationIntensity.clone();
		this.localSearchPhase = (ArrayList<Integer>) copy.localSearchPhase.clone();
		this.depthOfSearch = (ArrayList<Double>) copy.depthOfSearch.clone();
	}
	
	//Constructor
	public HeuristicParameters()
	{
		this.perturbationPhase = new ArrayList<Integer>();		
		this.mutationIntensity = new ArrayList<Double>();
		this.localSearchPhase = new ArrayList<Integer>();
		this.depthOfSearch = new ArrayList<Double>();
	}
	
	//get function for pertubationPhase parameters list
	ArrayList<Integer> getPerturbationPhase()
	{
		return this.perturbationPhase;
	}
	
	//get function for the mutationIntensity parameters list
	ArrayList<Double> getmutationIntensity()
	{
		return this.mutationIntensity;
	}
	
	//get function for the localSearchPhase parameters list 
	ArrayList<Integer> getLocalSearchPhase()
	{
		return this.localSearchPhase;
	}
	
	//get function for the depthOfSearch parameters list
	ArrayList<Double> getdepthOfSearch()
	{
		return this.depthOfSearch;
	}
	
	/*
	 * @paramteres
	 * problem : object representing the current problem
	 * populationSize
	 * heuristicTypes :HashMap containing the map between
	 * 	  Heuristic ID and Heuristic Type (i.e LOCAL_SEARCH,
	 *    MUTATION, RUIN_RECREATE and CROSSOVER
	 * index1
	 * index2
	 * resultantIndex : Index where the offspring is stored. 
	 */
	public int applyPattern(ProblemDomain problem, int populationSize, HashMap<Integer, HeuristicType> heuristicTypes,
						int index1, int index2,int resultantIndex)
	{
		
		//Applying perturbation heuristics like CROSSOVER, MUTATION and Ruin Recreate to the solution
		if(heuristicTypes.get(this.perturbationPhase.get(0))==HeuristicType.CROSSOVER)
		{
			problem.setIntensityOfMutation(this.mutationIntensity.get(0));
			problem.applyHeuristic(this.perturbationPhase.get(0),index1,index2,resultantIndex);
		}else if(heuristicTypes.get(this.perturbationPhase.get(0))==HeuristicType.MUTATION)
		{
			problem.setIntensityOfMutation(this.mutationIntensity.get(0));
			problem.applyHeuristic(this.perturbationPhase.get(0),index1, resultantIndex);
		}else if(heuristicTypes.get(this.perturbationPhase.get(0))==HeuristicType.RUIN_RECREATE){
			problem.setIntensityOfMutation(this.mutationIntensity.get(0));
			problem.applyHeuristic(this.perturbationPhase.get(0),index1, resultantIndex);
		}
		
		if(this.perturbationPhase.size() > 1)
		{
			problem.setIntensityOfMutation(this.mutationIntensity.get(1));
			problem.applyHeuristic(this.perturbationPhase.get(1),resultantIndex, resultantIndex);
		}
		
		//Applying Local Search Heuristics to the solution
		int localSearchPhaseSize = this.localSearchPhase.size();
		for(int i=0; i < localSearchPhaseSize; i++)
		{
			problem.setDepthOfSearch(this.depthOfSearch.get(i));
			problem.applyHeuristic(this.localSearchPhase.get(i),resultantIndex, resultantIndex);
		}
		
		return resultantIndex;
	}
}
