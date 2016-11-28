package org.ak80.akkabase;

import akka.actor.ActorSystem;

/**
 * Main class to run akkabase db
 */
public class Launcher {

  private static Launcher launcher = new Launcher(ActorSystem.create("akkabase"));
  private final ActorSystem system;

  public Launcher(ActorSystem system) {
    this.system = system;
  }

  public void run(String[] args) {
    system.actorOf(AkkabaseDb.create(), "akkabase-db");
  }

  public static void main(String[] args) {
    launcher.run(args);
  }

  static void setLauncher(Launcher launcher) {
    Launcher.launcher = launcher;
  }
  
}

