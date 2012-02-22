package aurora.ide.meta.gef.editors.models;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class ViewDiagram extends Container {
	private static final long serialVersionUID = -9196440587781890208L;
	public static final int DLabelWidth = 80;
	private static Class<?>[] unsupported = { Toolbar.class, Navbar.class,
			GridColumn.class, TabItem.class };

	private List<Dataset> datasets = new ArrayList<Dataset>();
	private String bindTemplate = "";

	@Override
	public boolean isResponsibleChild(AuroraComponent component) {
		Class<?> cls = component.getClass();
		for (Class<?> c : unsupported)
			if (c.equals(cls))
				return false;
		return super.isResponsibleChild(component);
	}

	/**
	 * @deprecated
	 */
	public List<Dataset> getDatasets() {
		return datasets;
	}

	/**
	 * @deprecated
	 */
	public void setDatasets(List<Dataset> datasets) {
		this.datasets = datasets;
	}

	/**
	 * @deprecated
	 */
	public void addDataset(Dataset ds) {
		datasets.add(ds);
		this.addChild(ds);
	}

	/**
	 * @deprecated
	 */
	public void removeDataset(Dataset ds) {
		datasets.remove(ds);
		this.removeChild(ds);
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return new IPropertyDescriptor[0];
	}

	public String getBindTemplate() {
		return bindTemplate;
	}

	public void setBindTemplate(String bindTemplate) {
		this.bindTemplate = bindTemplate;
	}

	public boolean isBindTemplate() {
		return bindTemplate != null && !"".equals(bindTemplate.trim());
	}

	public List<String> getModels() {
		List<String> models = new ArrayList<String>();
		List<Container> containers = getContainers(this);
		for (Container container : containers) {
			String sectionType = container.getSectionType();
			if (Container.SECTION_TYPE_QUERY.equals(sectionType)
					|| Container.SECTION_TYPE_RESULT.equals(sectionType)) {
				String model = container.getDataset().getModel();
				if (null != model) {
					models.add(model);
				}
			}
		}
		return models;
	}

	public List<Container> getContainers(Container container) {
		List<Container> containers = new ArrayList<Container>();
		List<AuroraComponent> children = container.getChildren();
		for (AuroraComponent ac : children) {
			if (ac instanceof Container) {
				containers.add((Container) ac);
				containers.addAll(getContainers((Container) ac));
			}
		}
		return containers;
	}

}