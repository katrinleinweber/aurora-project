package aurora.ide.meta.gef.editors.source.gen;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

import uncertain.composite.CompositeMap;
import aurora.ide.meta.gef.editors.models.Input;

public class InputMap extends AbstractComponentMap {

	private Input input;

	public InputMap(Input c) {
		this.input = c;
	}

	@Override
	public CompositeMap toCompositMap() {
		AuroraComponent2CompositMap a2c = new AuroraComponent2CompositMap();
		String type = input.getType();
		CompositeMap map = a2c.createChild(type);
		IPropertyDescriptor[] propertyDescriptors = input
				.getPropertyDescriptors();
		for (IPropertyDescriptor iPropertyDescriptor : propertyDescriptors) {
			Object id = iPropertyDescriptor.getId();

			boolean isKey = this.isCompositMapKey(id.toString());
			if (isKey) {
				Object value = "";
				if (Input.TYPECASE.equals(id))
					value = input.getTypeCase();
				else if (Input.ENABLEBESIDEDAYS.equals(id))
					value = input.getEnableBesideDays();
				else if (Input.ENABLEMONTHBTN.equals(id))
					value = input.getEnableMonthBtn();
				else {
					value = input.getPropertyValue(id).toString();
				}
				if (value != null && !("".equals(value)))
					map.putString(id, value.toString());
			}
		}
		return map;
	}

	@Override
	public boolean isCompositMapKey(String key) {
		return true;
	}

}