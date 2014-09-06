package com.talool.website.panel.merchant.definition;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import com.talool.core.MerchantCodeGroup;
import com.talool.core.service.ServiceException;
import com.talool.domain.MerchantCodeGroupImpl;
import com.talool.website.models.MerchantCodeGroupModel;
import com.talool.website.panel.BaseDefinitionPanel;
import com.talool.website.panel.SubmitCallBack;

public class TrackingCodeMetaPanel extends BaseDefinitionPanel {

	public TrackingCodeMetaPanel(String id, SubmitCallBack callback, final String code) {
		super(id, callback);
		setDefaultModel(new MerchantCodeGroupModel(code));
	}

	private static final long serialVersionUID = 3994401971909118276L;
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		form.add(new TextField<String>("codeGroupTitle"));
		form.add(new TextField<String>("codeGroupNotes"));
	}

	@SuppressWarnings("unchecked")
	@Override
	public CompoundPropertyModel<?> getDefaultCompoundPropertyModel() {
		return new CompoundPropertyModel<MerchantCodeGroup>((IModel<MerchantCodeGroup>) getDefaultModel());
	}

	@Override
	public String getObjectIdentifier() {
		final MerchantCodeGroup mcg = (MerchantCodeGroup) form.getDefaultModelObject();
		return mcg.getId().toString();
	}

	@Override
	public void save() throws ServiceException {
		MerchantCodeGroupImpl mcg = (MerchantCodeGroupImpl) form.getDefaultModelObject();
		if (StringUtils.isEmpty(mcg.getCodeGroupTitle()))
		{
			mcg.setCodeGroupTitle(" ");
		}
		taloolService.merge(mcg);
	}

	@Override
	public String getSaveButtonLabel() {
		return "Save";
	}

}
