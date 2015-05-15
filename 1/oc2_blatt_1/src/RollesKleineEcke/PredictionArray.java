package RollesKleineEcke;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Rolle on 11.05.2015.
 */
public class PredictionArray {
    private HashMap<String ,PredictionArrayElement> pArray = new HashMap<String, PredictionArrayElement>();



    public PredictionArray(MatchSet mSet){
        this.createPredictionArray(mSet);
    }

    private void createPredictionArray(MatchSet mSet){
        HashMap<String ,ClassifierSet> helperMap = sortMatchSet(mSet);

        for(Map.Entry<String,ClassifierSet> entry : helperMap.entrySet()) {
            String action = entry.getKey();
            ClassifierSet cSet = entry.getValue();
            PredictionArrayElement newPElement = new PredictionArrayElement(cSet);
            pArray.put(action,newPElement);
        }
    }

    private HashMap<String ,ClassifierSet> sortMatchSet(MatchSet mSet){
        HashMap<String ,ClassifierSet> helperMap = new HashMap<String, ClassifierSet>();

         /*
            After the For loop, The Map schould look like that:
            Map = {
                "Action 1" : ClassifierSet // where every classifier in the set belongs to Action 1
                "Action 2" : ClassifierSet
            }

        */
        for(Classifier c: mSet.getSet()){
            String action = c.getAction();

            if(helperMap.containsKey(action))
                helperMap.get(action).addNewClassifier(c);
            else{
                ClassifierSet tmp = new ClassifierSet();
                tmp.addNewClassifier(c);
                helperMap.put(action, tmp);
            }
        }

        return helperMap;
    }


    public ActionSet getBestActionSet(){
        double maxValue = 0;
        PredictionArrayElement winner = null;

        for(Map.Entry<String,PredictionArrayElement> entry : pArray.entrySet()) {
            PredictionArrayElement currentElement = entry.getValue();
            double currentValue = currentElement.getCalculatedPredictionArrayValue();
            //System.out.println("Current Action: >" + currentElement.getAction() + "< ParrayValue: " +currentValue );
            if (maxValue < currentValue) {
                //System.out.println("New Max Found! old was: " + maxValue + " new is: " + currentValue);
                maxValue = currentValue;
                winner = currentElement;
            }
        }

        ClassifierSet winnerSet = pArray.get(winner.getAction()).getClassifierSet();
        return new ActionSet(winnerSet);
    }



    private class PredictionArrayElement {

        public PredictionArrayElement(ClassifierSet classifierSet) {
            this.classifierSet = classifierSet;
            calculatePredictionArrayValue();
            setMyAction();
        }

        private ClassifierSet classifierSet;
        private String action;
        private Double calculatedPredictionArrayValue;

        private void calculatePredictionArrayValue(){
            double sumPreTimesFit = 0;
            double sumFitness = 0;

            for(Classifier c : classifierSet.getSet()){
                sumPreTimesFit += c.getPrediction() * c.getFitness() ;
                sumFitness += c.getFitness();
            }
            calculatedPredictionArrayValue = sumPreTimesFit / sumFitness;
        }

        private void setMyAction(){
            action = classifierSet.getSet().get(0).getAction();
        }

        public Double getCalculatedPredictionArrayValue() {
            return calculatedPredictionArrayValue;
        }

        public String getAction() {
            return action;
        }

        public ClassifierSet getClassifierSet(){
            return this.classifierSet;
        }

    }
}
