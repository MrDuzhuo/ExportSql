package exportncsql.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import exportncsql.view.SqlExportSWTUI;

public class DqSqlExportAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;

	public void run(IAction action) {
		Shell shell = new Shell(this.window.getShell(), 64);
		shell.setText("UF NC SqlExport");

		shell.setLayout(new FillLayout());
		shell.setSize(new Point(560, 450));
		new SqlExportSWTUI(shell, 0);
		shell.pack();
		shell.open();
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}

	public void dispose() {
	}

	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
}