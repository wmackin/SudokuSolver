package solver;

import ui.SudokuView;

import java.util.ArrayList;

public interface SudokuObserver<Subject, ClientData> {
    void update(Subject subject, ClientData data);

    ArrayList<SudokuView.SudokuButton> getButtons();
}
