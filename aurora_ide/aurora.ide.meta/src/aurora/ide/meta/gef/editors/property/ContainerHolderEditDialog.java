package aurora.ide.meta.gef.editors.property;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import aurora.plugin.source.gen.screen.model.AuroraComponent;
import aurora.plugin.source.gen.screen.model.Container;
import aurora.plugin.source.gen.screen.model.ContainerHolder;
import aurora.plugin.source.gen.screen.model.IDialogEditableObject;
import aurora.plugin.source.gen.screen.model.ScreenBody;

public class ContainerHolderEditDialog extends EditWizard {
	private ContainerHolder containerHolder = null;
	private Container tmpTarget = null;

	public ContainerHolderEditDialog() {
		super();
		setWindowTitle("Container"); //$NON-NLS-1$
	}

	public void addPages() {
		addPage(new InnerPage("ContainerSelection")); //$NON-NLS-1$
	}

	@Override
	public void setDialogEdiableObject(IDialogEditableObject obj) {
		containerHolder = (ContainerHolder) obj;
		tmpTarget = containerHolder.getTarget();
	}

	@Override
	public boolean performFinish() {
		if (tmpTarget != null)
			containerHolder.setTarget(tmpTarget);
		return true;
	}

	private class InnerPage extends WizardPage {

		protected InnerPage(String pageName) {
			super(pageName);
			setTitle(Messages.QueryContainerEditDialog_2);
		}

		public void createControl(Composite parent) {
			AuroraComponent comp = (AuroraComponent) containerHolder
					.getContextInfo();
			ScreenBody root = null;
			while (comp != null) {
				if (comp instanceof ScreenBody) {
					root = (ScreenBody) comp;
					break;
				}
				comp = comp.getParent();
			}
			if (root == null)
				throw new RuntimeException("Null root"); //$NON-NLS-1$
			ModelTreeSelector mts = new ModelTreeSelector(parent, SWT.BORDER);
			TreeViewer tv = mts.getTreeViewer();
			tv.setFilters(new ViewerFilter[] {
					ModelTreeSelector.CONTAINER_FILTER,
					ModelTreeSelector.getSectionFilter(containerHolder
							.getContainerTypes()), new ViewerFilter() {

						@Override
						public boolean select(Viewer viewer,
								Object parentElement, Object element) {
							return containerHolder.getOwner().equals(element) == false;
						}

					} });
			final Tree tree = tv.getTree();
			mts.setRoot(root);
			mts.setSelection(tmpTarget);
			mts.refreshTree();
			tree.addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent e) {
					TreeItem ti = tree.getSelection()[0];
					tmpTarget = (Container) ti.getData();
				}

				public void widgetDefaultSelected(SelectionEvent e) {

				}
			});

			//
			setControl(tree);
		}
	}
}
