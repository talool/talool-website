package com.talool.website.panel.merchant;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.Session;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.RangeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.braintreegateway.MerchantAccount;
import com.braintreegateway.MerchantAccountRequest;
import com.braintreegateway.Result;
import com.talool.core.Merchant;
import com.talool.core.service.ServiceException;
import com.talool.payment.braintree.BraintreeUtil;
import com.talool.service.ServiceConfig;
import com.talool.utils.KeyValue;
import com.talool.utils.SafeSimpleDateFormat;
import com.talool.website.component.StateOption;
import com.talool.website.component.StateSelect;
import com.talool.website.panel.BaseTabPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.util.SessionUtils;

/**
 * A panel displaying sub-merchant information from Braintree
 * 
 * @author clintz
 * 
 */
public class FundraiserPaymentProcessingPanel extends BaseTabPanel
{
	private static final Logger LOG = LoggerFactory.getLogger(FundraiserPaymentProcessingPanel.class);
	private static final String BIRTH_DAY_FORMAT = "YYYY-MM-dd";
	private static final String SECURE_PREFIX_TEXT = "******";
	private static final SafeSimpleDateFormat bdayFormat = new SafeSimpleDateFormat(BIRTH_DAY_FORMAT);
	private static final SafeSimpleDateFormat STATUS_TIME_FORMAT = new SafeSimpleDateFormat(BIRTH_DAY_FORMAT + " HH:mm:ss Z");

	private static final long serialVersionUID = 9088580249227461453L;

	private final UUID fundraiserId;

	private IndividualDetails individualDetails;
	private BusinessDetails businessDetails;
	private FundingDetails fundingDetails;
	@SuppressWarnings("unused")
	private String statusMessage;
	@SuppressWarnings("unused")
	private Float transactionFeePercent;

	private static class BusinessDetails implements Serializable
	{
		private static final long serialVersionUID = -2332110509573373354L;
		String dbaName;
		String taxId;
	}

	private static class IndividualDetails implements Serializable
	{
		private static final long serialVersionUID = -6699346572673655140L;
		String firstName;
		String lastName;
		String email;
		String phone;
		Date birthDay;
		String ssn;
		String streetAddress;
		String city;
		StateOption state;
		String zip;
	}

	private static class FundingDetails implements Serializable
	{
		private static final long serialVersionUID = 8852167293847101045L;
		String emailAddr;
		String mobilePhone;
		String accountNumber;
		String routingNumber;
	}

	public FundraiserPaymentProcessingPanel(String id, PageParameters parameters)
	{
		super(id);
		fundraiserId = UUID.fromString(parameters.get("id").toString());
	}

	private void initializeSubmerchant(final Merchant fundraiser)
	{
		StringBuilder sb = new StringBuilder();
		individualDetails = new IndividualDetails();
		businessDetails = new BusinessDetails();
		fundingDetails = new FundingDetails();

		transactionFeePercent = fundraiser.getProperties().getAsFloat(KeyValue.percentage);
		String braintreeStatusMessage = fundraiser.getProperties().getAsString(KeyValue.braintreeSubmerchantStatusMessage);

		String statusProp = fundraiser.getProperties().getAsString(KeyValue.braintreeSubmerchantStatus);

		if (statusProp == null)
		{
			sb.append(fundraiser.getName()).append(" is not currently setup as a Sub Merchant account in Braintree.");
			statusMessage = sb.toString();
			return;
		}

		// The submerchant account is either pending or active in braintree, lets
		// fetch the data
		Date statusTime = new Date(fundraiser.getProperties().getAsLong(KeyValue.braintreeSubmerchantStatusTimestamp));
		MerchantAccount.Status status = MerchantAccount.Status.valueOf(statusProp);

		sb.append("The Braintree Sub Merchant account status for ").append(fundraiser.getName()).append(" is ").append(status.toString())
				.append(".  This status was set on ").append(STATUS_TIME_FORMAT.format(statusTime));

		if (status == MerchantAccount.Status.PENDING)
		{
			sb.append("  Production account approvals can take a few minutes.");
		}

		statusMessage = sb.toString();

		if (StringUtils.isNotEmpty(braintreeStatusMessage))
		{
			sb.append(braintreeStatusMessage);
		}

		MerchantAccount merchantAccount = BraintreeUtil.get().findMerchantAccount(
				fundraiser.getProperties().getAsString(KeyValue.braintreeSubmerchantId));

		businessDetails.dbaName = merchantAccount.getBusinessDetails().getDbaName();
		businessDetails.taxId = merchantAccount.getBusinessDetails().getTaxId();

		try
		{
			individualDetails.birthDay = bdayFormat.parse(merchantAccount.getIndividualDetails().getDateOfBirth());
		}
		catch (ParseException e)
		{
			LOG.error("Could not parse bday", e);
		}

		individualDetails.streetAddress = merchantAccount.getIndividualDetails().getAddress().getStreetAddress();
		individualDetails.city = merchantAccount.getIndividualDetails().getAddress().getLocality();
		individualDetails.state = StateSelect.getStateOptionByCode(merchantAccount.getIndividualDetails().getAddress().getRegion());
		individualDetails.zip = merchantAccount.getIndividualDetails().getAddress().getPostalCode();
		individualDetails.email = merchantAccount.getIndividualDetails().getEmail();
		individualDetails.firstName = merchantAccount.getIndividualDetails().getFirstName();
		individualDetails.lastName = merchantAccount.getIndividualDetails().getLastName();
		individualDetails.phone = merchantAccount.getIndividualDetails().getPhone();
		// TODO Braintree does not return the SSN at all - send email to them and
		// explain
		individualDetails.ssn = SECURE_PREFIX_TEXT;

		fundingDetails.accountNumber = "******" + merchantAccount.getFundingDetails().getAccountNumberLast4();
		fundingDetails.emailAddr = merchantAccount.getFundingDetails().getEmail();
		fundingDetails.mobilePhone = merchantAccount.getFundingDetails().getMobilePhone();
		fundingDetails.routingNumber = merchantAccount.getFundingDetails().getRoutingNumber();

	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		try
		{
			final Merchant fundraiser = taloolService.getMerchantById(fundraiserId);

			add(new Label("statusMessage", new PropertyModel<String>(this, "statusMessage")));

			Form<Void> form = new Form<Void>("form");
			add(form);

			form.add(new TextField<Float>("transactionFeePercent", new PropertyModel<Float>(this, "transactionFeePercent"))
					.setRequired(true).add(RangeValidator.range(1.0f, 100.0f)));

			initializeSubmerchant(fundraiser);

			TextField<String> nameText = new TextField<String>("name", Model.of(fundraiser.getName()));
			form.add(nameText);

			String address = fundraiser.getPrimaryLocation().getNiceStreetAddress();
			TextField<String> addressText = new TextField<String>("address", Model.of(address));
			form.add(addressText);

			TextField<String> cityText = new TextField<String>("city", Model.of(fundraiser.getPrimaryLocation().getCity()));
			form.add(cityText);

			StateSelect stateSelect = new StateSelect("state", Model.of(StateSelect.getStateOptionByCode(fundraiser.getPrimaryLocation()
					.getStateProvinceCounty())));

			form.add(stateSelect);

			TextField<String> zipText = new TextField<String>("zip", Model.of(fundraiser.getPrimaryLocation().getZip()));
			form.add(zipText);

			TextField<String> taxIdText = new TextField<String>("taxId", new PropertyModel<String>(businessDetails, "taxId"));
			form.add(taxIdText.setRequired(true));

			TextField<String> dbaNameText = new TextField<String>("dbaName", new PropertyModel<String>(businessDetails, "dbaName"));
			form.add(dbaNameText.setRequired(true));

			WebMarkupContainer individualDetailsContainer = new WebMarkupContainer("individualDetails",
					new CompoundPropertyModel<IndividualDetails>(individualDetails));
			form.add(individualDetailsContainer);
			individualDetailsContainer.add(new TextField<String>("firstName").setRequired(true));
			individualDetailsContainer.add(new TextField<String>("lastName").setRequired(true));
			individualDetailsContainer.add(new TextField<String>("email").setRequired(true));
			individualDetailsContainer.add(new TextField<String>("phone"));
			individualDetailsContainer.add(new TextField<String>("ssn"));
			individualDetailsContainer.add(DateTextField.forDatePattern("birthDay", BIRTH_DAY_FORMAT).setRequired(true));
			individualDetailsContainer.add(new TextField<String>("streetAddress").setRequired(true));
			individualDetailsContainer.add(new TextField<String>("city").setRequired(true));
			individualDetailsContainer.add(new StateSelect("state").setRequired(true));
			individualDetailsContainer.add(new TextField<String>("zip").setRequired(true));

			WebMarkupContainer fundingDetailsContainer = new WebMarkupContainer("accountFunding", new CompoundPropertyModel<FundingDetails>(
					fundingDetails));
			form.add(fundingDetailsContainer);
			fundingDetailsContainer.add(new TextField<String>("emailAddr").setRequired(true));
			fundingDetailsContainer.add(new TextField<String>("mobilePhone").setRequired(true));
			fundingDetailsContainer.add(new TextField<String>("accountNumber").setRequired(true));
			fundingDetailsContainer.add(new TextField<String>("routingNumber").setRequired(true));

			form.add(new SubmitLink("submitLink")
			{
				private static final long serialVersionUID = 8842918221454890363L;

				@Override
				public void onSubmit()
				{
					StringBuilder sb = new StringBuilder();
					// target.add(((BasePage) this.getPage()).getFeedback());
					String masterMerchantAccountId = ServiceConfig.get().getBraintreeMasterMerchantId();

					MerchantAccountRequest request = new MerchantAccountRequest().individual().firstName(individualDetails.firstName)
							.lastName(individualDetails.lastName).email(individualDetails.email).phone(individualDetails.phone)
							.dateOfBirth(bdayFormat.format(individualDetails.birthDay)).ssn(individualDetails.ssn).address()
							.streetAddress(individualDetails.streetAddress).locality(individualDetails.city)
							.region(individualDetails.state.getCode()).postalCode(individualDetails.zip).done().done().business()
							.legalName(fundraiser.getName()).dbaName(businessDetails.dbaName).taxId(businessDetails.taxId).address()
							.streetAddress(fundraiser.getPrimaryLocation().getNiceStreetAddress())
							.locality(fundraiser.getPrimaryLocation().getCity()).region(fundraiser.getPrimaryLocation().getStateProvinceCounty())
							.postalCode(fundraiser.getPrimaryLocation().getZip()).done().done().funding()
							.destination(MerchantAccount.FundingDestination.BANK).email(fundingDetails.emailAddr)
							.mobilePhone(fundingDetails.mobilePhone).accountNumber(fundingDetails.accountNumber)
							.routingNumber(fundingDetails.routingNumber).done().tosAccepted(true).masterMerchantAccountId(masterMerchantAccountId);

					if (fundraiser.getProperties().getAsString(KeyValue.braintreeSubmerchantStatus) == null)
					{
						// we are onboarding the sub merchant
						Result<MerchantAccount> result = BraintreeUtil.get().onboardSubMerchant(request);
						if (result.isSuccess())
						{
							// our UUID is too large for Braintrees merchantId, so we cannot
							// set the merchantId, we must get it from Braintree
							fundraiser.getProperties().createOrReplace(KeyValue.braintreeSubmerchantStatusTimestamp,
									Calendar.getInstance().getTimeInMillis());
							fundraiser.getProperties().createOrReplace(KeyValue.braintreeSubmerchantId, result.getTarget().getId());
							fundraiser.getProperties().createOrReplace(KeyValue.braintreeSubmerchantStatus,
									result.getTarget().getStatus().toString());
							LOG.info(result.getMessage());
							try
							{
								sb.append("Successfully sent to Braintree.  ").append(fundraiser.getName()).append(" Sub Merchant ID is ")
										.append(result.getTarget().getId());
								taloolService.saveProperties(fundraiser, fundraiser.getProperties());
								Session.get().info(sb.toString());
							}
							catch (ServiceException e)
							{
								LOG.error("Problem saving properties", e);
								SessionUtils.errorMessage("Problem saving properties");
							}
						}
						else
						{
							Session.get().error(result.getMessage());
						}

						return;

					}
					else
					{
						// we are editing an already onboarded sub merchant

						// TODO optimize this by a Momento or other pattern (only sending
						// changed data)

						Result<MerchantAccount> result = BraintreeUtil.get().updateMerchantAccount(
								fundraiser.getProperties().getAsString(KeyValue.braintreeSubmerchantId), request);

						if (result.isSuccess())
						{
							Session.get().info("Successfully updated and sent to Braintree");
						}
						else
						{
							SessionUtils.infoMessage("There was a problem updating: ", result.getMessage());
						}
					}

				}

			});

		}
		catch (ServiceException e)
		{
			org.apache.wicket.Session.get().error(e.getLocalizedMessage());
			LOG.error(e.getLocalizedMessage(), e);
		}

	}

	@Override
	public boolean hasActionLink()
	{
		return false;
	}

	@Override
	public String getActionLabel()
	{
		return null;
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback)
	{
		return null;
	}

}
