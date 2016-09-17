package com.jianglibo.vaadin.dashboard.uicomponent.gridfield;

import java.util.Collection;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.jianglibo.vaadin.dashboard.util.StyleUtil;
import com.vaadin.event.ShortcutListener;
import com.vaadin.data.Property;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public abstract class AddableScalarGridField<C extends Collection<B>, B extends Object>  extends ScalarGridField<C, B>{

	public AddableScalarGridField(Domains domains, Class<B> clazz, MessageSource messageSource, VaadinTableWrapper vtw,
			VaadinFormFieldWrapper vffw) {
		super(domains, clazz, messageSource, vtw, vffw);
	}

	@Override
	protected Component initContent() {
		VerticalLayout vl = new VerticalLayout();
		vl.addStyleName("viewheader");
		vl.setSizeFull();
		vl.addComponent(getGrid());
		HorizontalLayout hl = new HorizontalLayout();
		hl.setWidth("100%");
		
		ScalarProperty property = new ScalarProperty();
		TextField tf = new TextField(property);
		tf.setWidth("100%");
		tf.setNullRepresentation("");
		tf.addShortcutListener(new ShortcutListener("submit", null, KeyCode.ENTER) {
			@Override
			public void handleAction(Object sender, Object target) {
				aboutToAddValue(property);
			}
		});
		tf.setInputPrompt(MsgUtil.getMsgFallbackToSelf(getMessageSource(), "addablegridfield.addnew.", "prompt"));
		Button btn = new Button(MsgUtil.getMsgFallbackToSelf(getMessageSource(), "addablegridfield.addnew.", "btn"));
		btn.setWidthUndefined();
		
		btn.addClickListener(event -> {
			aboutToAddValue(property);
		});
		hl.addComponents(tf, btn);
		hl.addStyleName("toolbar");
		hl.setExpandRatio(tf, 3);
		StyleUtil.setMarginTopTen(hl);
		vl.addComponent(hl);
		return vl;
	}
	
	protected abstract void aboutToAddValue(Property<B> property);

	protected class ScalarProperty implements Property<B> {
		
		private B value;

		@Override
		public B getValue() {
			return value;
		}

		@Override
		public void setValue(B newValue) throws com.vaadin.data.Property.ReadOnlyException {
			this.value = newValue;
		}

		@Override
		public Class<? extends B> getType() {
			return getClazz();
		}

		@Override
		public boolean isReadOnly() {
			return false;
		}

		@Override
		public void setReadOnly(boolean newStatus) {
		}
		
	}
}
