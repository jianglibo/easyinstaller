package com.jianglibo.vaadin.dashboard.view.kvv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.Kkv;
import com.jianglibo.vaadin.dashboard.repositories.BoxRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.grid.BaseGridView;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;

@SpringView(name = KkvListView.VIEW_NAME)
public class KkvListView extends BaseGridView<Kkv, KkvGrid> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(KkvListView.class);

	public static final String VIEW_NAME = "kkv";

	public static final FontAwesome ICON_VALUE = FontAwesome.DESKTOP;

	
	@Autowired
	public KkvListView(BoxRepository repository,Domains domains, MessageSource messageSource,
			ApplicationContext applicationContext) {
		super(applicationContext, messageSource, domains, Kkv.class, KkvGrid.class);
	}

	
//	public void whenTotalPageChange(PageMetaEvent tpe) {
//		getTable().setColumnFooter("createdAt", String.valueOf(tpe.getTotalRecord()));	
//	}
	
	@Override
	public void onDynButtonClicked(ButtonDescription btnDesc) {
//		Collection<Box> selected;
//		switch (btnDesc.getItemId()) {
//		case CommonMenuItemIds.DELETE:
//			selected = (Collection<Box>) getTable().getValue();
//			selected.forEach(b -> {
//				if (b.isArchived()) {
//					getRepository().delete(b);
//				} else {
//					b.setArchived(true);
//					getRepository().save(b);
//				}
//			});
//			((BoxContainer)getTable().getContainerDataSource()).refresh();
//			break;
//		case CommonMenuItemIds.REFRESH:
//			((BoxContainer)getTable().getContainerDataSource()).refresh();
//			break;
//		case CommonMenuItemIds.EDIT:
//			selected = (Collection<Box>) getTable().getValue();
//			UI.getCurrent().getNavigator().navigateTo(VIEW_NAME + "/edit/" + selected.iterator().next().getId() + "?pv=" + getLvfb().toNavigateString());
//			break;
//		case CommonMenuItemIds.ADD:
//			UI.getCurrent().getNavigator().navigateTo(VIEW_NAME + "/edit");
//			break;
//		case "installedSoftware":
//			selected = (Collection<Box>) getTable().getValue();
//			UI.getCurrent().getNavigator().navigateTo(InstallListView.VIEW_NAME + "/?boxid=" + selected.iterator().next().getId() + "&pv=" + getLvfb().toNavigateString());
//			break;
//		default:
//			LOGGER.error("unKnown menuName {}", btnDesc.getItemId());
//		}
	}

	@Override
	protected KkvGrid createGrid(MessageSource messageSource, Domains domains, Class<Kkv> clazz) {
		return new KkvGrid(messageSource, domains, clazz);
	}
}
