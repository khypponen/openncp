/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/

package se.sb.epsos.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.feedback.IFeedback;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.ListModel;

import se.sb.epsos.web.model.ErrorFeedback;
import se.sb.epsos.web.util.CustomDataTable;
import se.sb.epsos.web.util.EpsosWebConstants;

/**
 * Panel to display error information to the end user.
 * 
 * @author Daniel
 * 
 */
public class ErrorFeedbackPanel extends Panel implements IFeedback {

	private static final long serialVersionUID = 1L;

	public ErrorFeedbackPanel(String id, final ListModel<ErrorFeedback> listModel) {
		super(id, listModel);
		List<IColumn<ErrorFeedback>> columns = new ArrayList<IColumn<ErrorFeedback>>();
		columns.add(new PropertyColumn<ErrorFeedback>(new StringResourceModel("label.errorCode", this, null), null, "errorCode"));
		columns.add(new PropertyColumn<ErrorFeedback>(new StringResourceModel("label.severityType", this, null), null, "severityType"));
		columns.add(new PropertyColumn<ErrorFeedback>(new StringResourceModel("label.errorMessage", this, null), null, "errorMessage"));
		SortableDataProvider<ErrorFeedback> provider = new SortableDataProvider<ErrorFeedback>() {
			private static final long serialVersionUID = 1L;

			public Iterator<ErrorFeedback> iterator(int start, int count) {
				if (listModel.getObject() == null || listModel.getObject().isEmpty()) {
					return Collections.<ErrorFeedback> emptyList().iterator();
				} else {
					return listModel.getObject().subList(start, start + count).iterator();
				}
            }

            public int size() {
            	return listModel.getObject() != null ? listModel.getObject().size() : 0;
            }

            public IModel<ErrorFeedback> model(ErrorFeedback error) {
                return Model.of(error);
            }
        };
        add(new CustomDataTable<ErrorFeedback>("dataTable", columns, provider, EpsosWebConstants.DATATABLE_DEFAULT_PAGE_SIZE).setOutputMarkupId(true));
	}
}
