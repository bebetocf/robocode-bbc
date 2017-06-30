package bbcTEAM;

import java.util.HashMap;
import java.util.Vector;
import java.awt.Color;
import java.io.IOException;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;
import org.drools.runtime.rule.QueryResultsRow;
import robocode.*;


public class MarretaDroid extends TeamRobot implements Droid {

    public static String RULES_FILE = "bbcTEAM/rules/droid_rules.drl";
    public static String CONSULT_ACTIONS = "consult_actions";
    
    // KBUILDER : knowledge builder. Takes a .drl file in INPUT and OUTPUTS a knowledge package that the kbase can handle
    private KnowledgeBuilder kbuilder;
    
    // KBASE : knowledge base. Contains rules, processes, functions and types models
    // Does not contain runtime infos.
    private KnowledgeBase kbase;
    
    // KSESSION : class used to dialog with the rules engine
    private StatefulKnowledgeSession ksession;
    
    
    private Vector<FactHandle> currentReferencedFacts = new Vector<FactHandle>();

    
    public MarretaDroid(){
    }
    

    public void run() {
    	DEBUG.enableDebugMode(System.getProperty("robot.debug", "true").equals("true"));

    	// Creates a knowledge base
    	createKnowledgeBase();
    	
        // COLORS
        setBodyColor(Color.red);
        setGunColor(Color.black);
        setRadarColor(Color.red);
        setBulletColor(Color.blue);

        // Make any movement from tank, radar or gun independent
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);
        setAdjustRadarForRobotTurn(true);


        while (true) {
//        	turnRadarRight(360);
        	DEBUG.message("TURN BEGINS");
            loadRobotState();// HEHRHUHDUHDHUDHUDUHDSHUSUH
            loadBattleState();

            // Fire rules
            DEBUG.message("Facts in active memory");
            DEBUG.printFacts(ksession);           
            ksession.fireAllRules();
            cleanAnteriorFacts();

            // Get Actions
            Vector<Action> actions = loadActions();
            DEBUG.message("Resulting Actions");
            DEBUG.printActions(actions);

            // Execute Actions
            executeActions(actions);
        	DEBUG.message("TURN ENDS\n");
            execute();  // Informs the robocode that the turn ended (blocking call)

        }

    }


    private void createKnowledgeBase() {
        String rulesFile = System.getProperty("robot.rules", MarretaDroid.RULES_FILE);

        DEBUG.message("Creating knowledge base");
        kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        
        DEBUG.message("Loading rules since "+rulesFile);
        kbuilder.add(ResourceFactory.newClassPathResource(rulesFile, MarretaDroid.class), ResourceType.DRL);
        if (kbuilder.hasErrors()) {
            System.err.println(kbuilder.getErrors().toString());
        }

        kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
        
        DEBUG.message("Creating session)");
        ksession = kbase.newStatefulKnowledgeSession();
        
      //Insere uma HashMap na memória de trabalho
        ksession.setGlobal("Enemies", new HashMap<String,EnemyRobot>());
    }



    private void loadRobotState() {
    	RobotState robotState = new RobotState(this);
    	currentReferencedFacts.add(ksession.insert(robotState));
    }

    private void loadBattleState() {
        BattleState battleState =
                new BattleState(
                		getBattleFieldWidth(), 
                		getBattleFieldHeight(),
                		getNumRounds(), 
                		getRoundNum(),
                		getTime(),
                		getOthers()
        );
        currentReferencedFacts.add(ksession.insert(battleState));
    }

    private void cleanAnteriorFacts() {
        for (FactHandle referencedFact : this.currentReferencedFacts) {
            ksession.retract(referencedFact);
        }
        this.currentReferencedFacts.clear();
    }

    private Vector<Action> loadActions() {
        Action action;
        Vector<Action> actionsList = new Vector<Action>();

        for (QueryResultsRow result : ksession.getQueryResults(MarretaDroid.CONSULT_ACTIONS)) {
            action = (Action) result.get("action");  			// get the Action object
            action.setRobot(this);                      		// link it to the current robot
            actionsList.add(action);
            ksession.retract(result.getFactHandle("action")); 	// clears the fact from the active memory
        }

        return actionsList;
    }

    private void executeActions(Vector<Action> actions) {
        for (Action action : actions) {
            action.initExecution();
        }
    }

    // Insert in memory the different happening events
    public void onBulletHit(BulletHitEvent event) {
    	currentReferencedFacts.add(ksession.insert(event));
    }

    public void onBulletHitBullet(BulletHitBulletEvent event) {
    	currentReferencedFacts.add(ksession.insert(event));
    }

    public void onBulletMissed(BulletMissedEvent event) {
    	currentReferencedFacts.add(ksession.insert(event));
    }

    public void onHitByBullet(HitByBulletEvent event) {
    	currentReferencedFacts.add(ksession.insert(event));
    }

    public void onHitRobot(HitRobotEvent event) {
    	currentReferencedFacts.add(ksession.insert(event));
    }

    public void onHitWall(HitWallEvent event) {
    	currentReferencedFacts.add(ksession.insert(event));
    }

    public void onRobotDeath(RobotDeathEvent event) {
    	currentReferencedFacts.add(ksession.insert(event));
    }
    
    public void onMessageReceived(MessageEvent event){
    	System.out.println(((EnemyRobot)event.getMessage()).getName());
    }
}
