package edu.ccrm;

import edu.ccrm.cli.CCRMCLI;

public class CCRMApp {
    public static void main(String[] args) {
        // Enable assertions check
        boolean assertionsEnabled = false;
        assert assertionsEnabled = true;
        
        System.out.println("=== Campus Course & Records Manager (CCRM) ===");
        System.out.println("Java SE Console Application");
        
        if (assertionsEnabled) {
            System.out.println("✓ Assertions are enabled");
        } else {
            System.out.println("⚠ Assertions are disabled - enable with -ea VM option");
        }
        
        try {
            CCRMCLI cli = new CCRMCLI();
            cli.start();
        } catch (Exception e) {
            System.err.println("Fatal error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}