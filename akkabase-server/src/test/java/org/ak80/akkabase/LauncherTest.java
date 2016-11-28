package org.ak80.akkabase;

import akka.actor.ActorSystem;
import akka.actor.Props;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class LauncherTest {

  private ArgumentCaptor<Props> propsCaptor = ArgumentCaptor.forClass(Props.class);

  @Test
  public void testMain() {
    // Given
    Launcher launcher = mock(Launcher.class);
    Launcher.setLauncher(launcher);
    String[] args = new String[]{"arg0", "arg1"};

    // When
    Launcher.main(args);

    // Then
    verify(launcher).run(args);
  }

  @Test
  public void testRun() {
    // Given
    ActorSystem actorSystem = mock(ActorSystem.class);
    Launcher launcher = new Launcher(actorSystem);

    // When
    launcher.run(new String[0]);

    // Then
    verify(actorSystem).actorOf(propsCaptor.capture(), eq("akkabase-db"));
    assertThat(propsCaptor.getValue().actorClass().getSimpleName(), is(AkkabaseDb.class.getSimpleName()));
  }

}
