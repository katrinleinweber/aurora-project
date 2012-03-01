package aurora.ide.meta.gef.editors.parts;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.AbstractLabeledBorder;
import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;

import aurora.ide.meta.gef.editors.figures.BoxFigure;
import aurora.ide.meta.gef.editors.models.BOX;
import aurora.ide.meta.gef.editors.models.IProperties;

public class BoxPart extends ContainerPart {

	@Override
	protected IFigure createFigure() {
		BoxFigure figure = new BoxFigure();
		BOX model = (BOX) getModel();
		figure.setBox(model);
		return figure;
	}

	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
	}

	protected void refreshVisuals() {
		BOX model = (BOX) getModel();
		BoxFigure figure = (BoxFigure) getFigure();
		Border border = figure.getBorder();
		if (border instanceof AbstractLabeledBorder) {
			((AbstractLabeledBorder) border).setLabel(model.getTitle());
		}
		super.refreshVisuals();
	}

	@Override
	protected void applyToModel() {
		super.applyToModel();
		Rectangle bounds = this.getFigure().getBounds();
		this.getComponent().applyToModel(bounds);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
		String prop = evt.getPropertyName();
		if (IProperties.ROW.equals(prop) || IProperties.COL.equals(prop)) {
			this.getFigure().revalidate();
		}
	}

	@Override
	public int getResizeDirection() {
		return NSEW;
	}

}
