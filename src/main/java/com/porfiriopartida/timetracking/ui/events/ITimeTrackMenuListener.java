package com.porfiriopartida.timetracking.ui.events;

import com.porfiriopartida.timetracking.app.Command;
import com.porfiriopartida.timetracking.app.Screen;

public interface ITimeTrackMenuListener  { //extends MenuListener
    void screenSelected(Screen screen);
    void executeCommand(Command command);
}
