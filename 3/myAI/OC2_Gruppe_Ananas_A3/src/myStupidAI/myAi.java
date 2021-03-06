package myStupidAI;

import jnibwapi.BWAPIEventListener;
import jnibwapi.JNIBWAPI;
import jnibwapi.model.Unit;
import jnibwapi.types.UnitType;
import myStupidAI.bolding.BoldingManager;

import java.util.HashSet;

/**
 * Created by Stefan Rudolph on 17.02.14.
 */
public class myAi implements BWAPIEventListener, Runnable {

    private final JNIBWAPI bwapi;

    private HashSet<Marine> marines;

    private HashSet<Unit> enemyUnits;

    private BoldingManager boldingManager;

    private int frame;
    private int marineID = 0;

    public myAi() {
        System.out.println("This is the ANANAS-AI! (-.(-.-).-)");
        System.out.println("    \\|/");
        System.out.println("    AXA");
        System.out.println("   /XXX\\");
        System.out.println("   \\XXX/");
        System.out.println("    '^'");
        bwapi = new JNIBWAPI(this, false);
    }

    public static void main(String[] args) {
        new myAi().run();
    }

    @Override
    public void matchStart() {
        bwapi.printText("Im a ANANAS!");

        marines = new HashSet<>();
        enemyUnits = new HashSet<>();

        this.boldingManager  = new BoldingManager();
        System.out.println("Current P-Set: " + boldingManager.getRuleMachine().getpSet().getMembersAsMap());

        frame = 0;

        bwapi.enablePerfectInformation();
        bwapi.enableUserInput();
        bwapi.setGameSpeed(1);

        int color = bwapi.getSelf().getColor();
        if(color == 111)
        	System.out.println("I'm the red Player!");
        else if(color == 165)
        	System.out.println("I'm the blue Player!");
    }

    @Override
    public void matchFrame() {

        for (Marine m : marines) {
            m.step(marines);
        }

        if (frame % 1000 == 0) {
            System.out.println("Frame: " + frame);
        }
        frame++;
    }

    @Override
    public void unitDiscover(int unitID) {
        Unit unit = bwapi.getUnit(unitID);
        int typeID = unit.getTypeID();

        if (typeID == UnitType.UnitTypes.Terran_Marine.getID()) {
            if (unit.getPlayerID() == bwapi.getSelf().getID()) {
                marines.add(new Marine(unit, bwapi, enemyUnits, marineID, boldingManager.getRuleMachine()));
                marineID++;
            } else {
                enemyUnits.add(unit);
            }
        } else if (typeID == UnitType.UnitTypes.Terran_Vulture.getID()) {
            if (unit.getPlayerID() != bwapi.getSelf().getID()) {
                enemyUnits.add(unit);
            }
        }
    }

    @Override
    public void unitDestroy(int unitID) {
        Marine rm = null;
        for (Marine marine : marines) {
            if (marine.getID() == unitID) {
                rm = marine;
                break;
            }
        }
        marines.remove(rm);

        Unit rmUnit = null;
        for (Unit u : enemyUnits) {
            if (u.getID() == unitID) {
                rmUnit = u;
                break;
            }
        }
        enemyUnits.remove(rmUnit);
    }

    @Override
    public void connected() {
        System.out.println("Connected");
    }

    @Override
    public void matchEnd(boolean winner) {
        int hpLeft = 0;
        for(Marine marine: marines)
            hpLeft += marine.getUnit().getHitPoints();
        boldingManager.gameFin(hpLeft);
    }

    @Override
    public void keyPressed(int keyCode) {

    }

    @Override
    public void sendText(String text) {

    }

    @Override
    public void receiveText(String text) {

    }

    @Override
    public void playerLeft(int playerID) {

    }

    @Override
    public void nukeDetect(int x, int y) {

    }



    @Override
    public void nukeDetect() {

    }

    @Override
    public void unitEvade(int unitID) {

    }

    @Override
    public void unitShow(int unitID) {

    }

    @Override
    public void unitHide(int unitID) {

    }

    @Override
    public void unitCreate(int unitID) {
    }

    @Override
    public void unitMorph(int unitID) {

    }

    @Override
    public void unitRenegade(int unitID) {

    }

    @Override
    public void saveGame(String gameName) {

    }

    @Override
    public void unitComplete(int unitID) {

    }

    @Override
    public void playerDropped(int playerID) {

    }

    @Override
    public void run() {
        bwapi.start();
    }
}


