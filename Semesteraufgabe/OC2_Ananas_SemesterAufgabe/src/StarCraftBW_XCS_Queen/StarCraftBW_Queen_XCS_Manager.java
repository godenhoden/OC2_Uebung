package StarCraftBW_XCS_Queen;

import General_XCS.PopulationSet;
import General_XCS.XCS_Constants;
import jnibwapi.Unit;

/**
 * Created by Papa on 09.07.2015.
 */
public class StarCraftBW_Queen_XCS_Manager {

    private StarCraftBW_QueenFileThread fileThread = new StarCraftBW_QueenFileThread();
    private StarCraftBW_Queen_XCS xcs;
    private StarCraftBW_QueenMatchStats mStats = new StarCraftBW_QueenMatchStats();
    private StarCraftBW_QueenDetector dDetector= new StarCraftBW_QueenDetector();
    private StarCraftBW_QueenEffector theEffector = new StarCraftBW_QueenEffector();
    private String[] actionSet = {"cast","kite"};

    public StarCraftBW_Queen_XCS_Manager() {
        this.xcs = new StarCraftBW_Queen_XCS(actionSet,theEffector,dDetector);
        fileThread.start();
    }

    public String getNextPredictedAction(){
        xcs.cleanCurrentAction();
        xcs.execPredictAction(true);
        return theEffector.getCurrentActionToExecute();
    }

    public void actionExecutionFin(Unit unit, Unit target, Double distance){
        theEffector.setStats(unit, target, distance);
        xcs.rewardCurrentAction();
    }

    private void setGA_Type(){
        PopulationSet pSet = xcs.getPopulationSet();
        pSet.setParent_select_method_type(XCS_Constants.GEN_ALGO_PARENT_ROULET);
        pSet.setCrossover_method_type(XCS_Constants.GEN_ALGO_CROSSOVER_RANDOM_ONE_POINT);
        pSet.setMutation_method_type(XCS_Constants.GEN_ALGO_MUTATION_RANDOM_ONE_POS);
    }

    public StarCraftBW_QueenDetector getDetector(){
        return this.dDetector;
    }

    public StarCraftBW_QueenEffector getEffector() {
        return this.theEffector;
    }

    public void makeNewMatchStat(int frames, int hp, int kills, int countAttackMove, int countKite) {
        this.mStats.makeNewMatchStat(frames,hp,kills,countAttackMove,countKite);
    }

    public void saveProgress(){
        try {
            fileThread.putClassifierSetToSave(xcs.getPopulationSet());
            fileThread.putMatchStatsToSave(this.mStats);

        } catch (InterruptedException e) {
            System.err.println("XCS_Manager: SAVING INTERUPTED");
        }
    }

    public void loadOldProgress(){
        loadOldPopulationSet();
        loadOldMatchStats();
    }

    private void loadOldPopulationSet(){
        PopulationSet newPSet = fileThread.getSavedPopulationSet();

        if (newPSet == null)
            return;

        else if(newPSet.getSet().size() > 0)
            xcs.setPopulationSet(newPSet);
    }

    private void loadOldMatchStats(){
        StarCraftBW_QueenMatchStats newMStats = fileThread.getSavedMatchStats();

        if (newMStats == null)
            return;

        else if(newMStats.getMatchStats().size() > 0)
            this.mStats = newMStats;
    }

    private void saveOnlyOnce(){
        saveProgress();
        fileThread.stopMe();
    }

    public void cleanUp(){
        saveOnlyOnce();
    }
}