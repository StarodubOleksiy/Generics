/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testvalidators;

import java.util.*;
 interface Task<T> {

        List<T> getResult();
}

interface Executor<T> {
     
        void addTask(Task task)throws Exception;
        // Добавить таск на выполнение и валидатор результата. Результат таска будет записан в ValidResults если validator.isValid вернет true для этого результата
		// Результат таска будет записан в InvalidResults если validator.isValid вернет false для этого результата
		// Бросает Эксепшн если уже был вызван метод execute()
        //void addTask(Task task, Validator validator);
        // Выполнить все добавленые таски
        
         void addTask(Task task, Validator<? super T> validator)throws Exception;
        void execute()throws Exception;
        // Получить валидные результаты. Бросает Эксепшн если не был вызван метод execute()
    List<T>getValidResults()throws Exception;
        // Получить невалидные результаты. Бросает Эксепшн если не был вызван метод execute()
    List<T>getInvalidResults()throws Exception;
    }


interface Validator<T> {
   boolean isValid(T value);
 }


//Task<Integer> testTask5 = new TaskImpl<Number>(listOfNumbers4);
class TaskImpl<T> implements Task {
    List<? extends T> result;
  //HashSet<Character> alphabet; 
  public TaskImpl(List<? extends T> values) {
  this.result = values;
  }   
    List<T> values;

    @Override
   public List<? extends T>  getResult()
    {
        return   result;
    }  
};

class ExecutorImpl<T> implements Executor {
private Deque<Task> taskQue;
List<T> validResult;
List<T> inValidResult;
Validator validator;
boolean calledExecute;
boolean calledAddTask;

public ExecutorImpl(Validator<? super T> validator)
{
    this.calledAddTask = false;
    this.calledExecute = false;
  taskQue = new LinkedList<>();  
  validResult = new ArrayList<>();
  inValidResult = new ArrayList<>();
  this.validator = validator;
}

public ExecutorImpl()
{
  this.calledAddTask = false;
  this.calledExecute = false;  
  taskQue = new LinkedList<>();  
  validResult = new ArrayList<>();
  inValidResult = new ArrayList<>();
}

@Override
public void addTask(Task task) throws Exception
{
    if(calledExecute) throw new Exception("Method execute was called!!!");
   this.calledAddTask = true; 
   taskQue.add(task);
}

@Override
public void addTask(Task task, Validator  validator)throws Exception
{
     if(calledExecute) throw new Exception("Method execute was called!!!");
     this.calledAddTask = true; 
    taskQue.add(task);
     this.validator = null; 
     this.validator = validator;        
}

@Override
public void execute()throws Exception
  {
  if(!calledAddTask) throw new Exception("Method addTask was not called!!!");     
 this.calledExecute = true;     
while(!taskQue.isEmpty()) {
List<T> values = taskQue.pop().getResult();
  for (T value : values) {
if (validator.isValid(value)) 
validResult.add(value);
 else
inValidResult.add(value);    
}
     
  }
    
}
@Override
public List<T>getValidResults()throws Exception {
    if(!calledExecute) throw new Exception("Method execute was not called!!!");
    return validResult;
}
      
@Override
 public List<T>getInvalidResults()throws Exception {
    if(!calledExecute) throw new Exception("Method execute was not called!!!");
   return inValidResult;   
  }

}  
class NumberValidator implements Validator<Number> {
 
 @Override
 public boolean isValid(Number number) {
   if(number.intValue() < 0 || number.intValue() > Integer.MAX_VALUE || (number.intValue())%2!=0)
     return false;
   return true;
    
 }
 }


public class TestValidators {

    public static void main(String[] args) {
        try
        {
       List<Number> listOfNumbers = new ArrayList();
       
       listOfNumbers.add(10);
        listOfNumbers.add(100);
         listOfNumbers.add(1000);
        listOfNumbers.add(new Integer(456));
       listOfNumbers.add(new Double(45.6));
       listOfNumbers.add(new Long(46));
       listOfNumbers.add(new Float(77.6));
       
        Task<Number> testTask = new TaskImpl<Number>(listOfNumbers); 
        Validator validator = new NumberValidator(); 
        Executor<Number> testExecutor = new  ExecutorImpl<Number>(validator);
        testExecutor.addTask(testTask);
        
        
        
        List<Number> listOfNumbers2 = new ArrayList();
       listOfNumbers2.add(56.45);
        listOfNumbers2.add(-45.12);
         listOfNumbers2.add(7.8);
         listOfNumbers2.add(-7);
         listOfNumbers2.add(-9L);
         listOfNumbers2.add(-10F);
        //  listOfNumbers2.add(new Double(""));
       
        Task<Number> testTask2 = new TaskImpl<Number>(listOfNumbers2); 
        //Validator validator = new NumberValidator(); 
        //Executor<Number> testExecutor2 = new  ExecutorImpl<Number>(validator);
        testExecutor.addTask(testTask2);
        
        
        Task<Integer> testTask3 = new TaskImpl<Integer>(Arrays.asList(2,4,8,6,9)); 
        testExecutor.addTask(testTask3);
                
        Task<Double> testTask4 = new TaskImpl<Double>(Arrays.asList(2.5,4.8,8.7,6.4,9.1)); 
        testExecutor.addTask(testTask4);
        
     //   testExecutor.execute();
        
         List<Integer> listOfNumbers3 = new ArrayList();
         listOfNumbers3.add(1);
         listOfNumbers3.add(2);
         listOfNumbers3.add(3);
         listOfNumbers3.add(4);
         listOfNumbers3.add(5);
        
         List<Double> listOfNumbers4 = new ArrayList();
          Task<Number> testTask5 = new TaskImpl<Number>(listOfNumbers4);
         listOfNumbers4.add(6.1);
         listOfNumbers4.add(7.2);
         listOfNumbers4.add(8.3);
         listOfNumbers4.add(9.4);
         listOfNumbers4.add(10.5);
         testExecutor.addTask(testTask5);
        // System.out.println(testExecutor.getValidResults());
        //System.out.println(testExecutor.getValidResults());
        List<Integer> listOfNumbers5 = new ArrayList();
          Task<Number> testTask6 = new TaskImpl<Number>(listOfNumbers5);
         listOfNumbers5.add(10);
         listOfNumbers5.add(11);
         listOfNumbers5.add(12);
         listOfNumbers5.add(13);
         listOfNumbers5.add(14);
         testExecutor.addTask(testTask6);
         testExecutor.execute();
         System.out.println("Valid results");
         System.out.println(testExecutor.getValidResults());
         System.out.println("Invalid results");
          System.out.println(testExecutor.getInvalidResults());
          
          Task<Number> testTask7 = new TaskImpl<Number>(Arrays.asList(1,2,3,4,5,6,7,8,9,10));
           Executor<Number> numberExecutor = new ExecutorImpl<Number>();
           numberExecutor.addTask(testTask7, validator);
           numberExecutor.execute();
         System.out.println("Valid results");
         System.out.println(numberExecutor.getValidResults());
         System.out.println("Invalid results");
          System.out.println(numberExecutor.getInvalidResults());
           //
        
        } catch(Exception exp)
        {
            System.out.println(exp.toString());
        }
    }
}
