//import BatchManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

import VRP.VRP;
//import AbstractClasses.
import AbstractClasses.ProblemDomain;
public class VehicleRoutingProblemStarter {

	/*
	 * @parameters
	 * timeLimitSeconds: It indicates the time limit for each run of HyperHeuristic
	 * 
	 * @description
	 * The function creates the instance of the Hyper Heuristic Algorithm.
	 * It sets the memory size, initializes initial solutions and loads the
	 * problem domain and initializes the low level heuristics and run
	 * the Hyper-Hueristic algorithm
	 */
	static void runHyperHeuristics(int timeLimitSeconds,int instanceID, int iterations) throws InterruptedException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		int seedValue=0;
		ProblemDomain problem = loadProblemInstance(seedValue,0);
		
		HyperHeuristicAlgorithm hyperHeuristicAlgorithm = new HyperHeuristicAlgorithm(seedValue);
		HyperHeuristicAlgorithm.problem= problem;
		
		hyperHeuristicAlgorithm.setTimeLimit(600*1000);
		hyperHeuristicAlgorithm.problem.setMemorySize(20002);
		hyperHeuristicAlgorithm.intializeSolutions(hyperHeuristicAlgorithm.problem);
		hyperHeuristicAlgorithm.loadProblemDomain(problem);
		hyperHeuristicAlgorithm.intializeLowLevelHueristicIndexes();
		
		hyperHeuristicAlgorithm.run(iterations);	
	}
	
	/*
	 * @parameters 
	 * seedValue : Initial seedValue for the problem.
	 * instanceID : Represents the VRP input file
	 * 
	 * @description
	 * The function takes seedValue and the instanceID representing
	 * the Input problem and loads into a ProblemDomain object
	 */
	static ProblemDomain loadProblemInstance(long seedValue, int instanceID) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		ProblemDomain problem = null;
		//loading the Vehicle Routing Problem class from the HyFlex Framework
		@SuppressWarnings("unchecked")
		Class<? extends ProblemDomain> pClass = VRP.class;
		Constructor<? extends ProblemDomain> objectReflector = pClass.getDeclaredConstructor(Long.TYPE);
		problem = (ProblemDomain)objectReflector.newInstance(seedValue);
		// Loading  problem instance			
		problem.loadInstance(instanceID);	
		return problem;
	}
	
	/*
	 * Start of the program
	 */
	public static void main(String[] args) throws InterruptedException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// TODO Auto-generated method stub
		//From the command prompt, get the problemID.
		Scanner sc= new Scanner(System.in);
		System.out.println("Enter the problem Instance ID. Enter number between 1-4.Currently it includes 4 input files");
		int instanceID = sc.nextInt();
		System.out.println("Enter the number of iterations");
		int iterations = sc.nextInt();
		
		Integer timeLimitSeconds = 600;
		Integer numberOfRuns = 1;
		
		for(int i =0; i < numberOfRuns;i++)
		{
			runHyperHeuristics(timeLimitSeconds,instanceID,iterations);
		}
		
	}

}
