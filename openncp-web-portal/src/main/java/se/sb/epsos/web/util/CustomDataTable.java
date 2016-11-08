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
package se.sb.epsos.web.util;

import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigationToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NoRecordsToolbar;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.OddEvenItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.sb.epsos.web.pages.QueryDocumentsPage;

public class CustomDataTable<T> extends DataTable<T> {
	private static Logger LOGGER = LoggerFactory.getLogger(QueryDocumentsPage.class);
	private static final long serialVersionUID = 1L;

	public CustomDataTable(String id, List<IColumn<T>> columns, ISortableDataProvider<T> dataProvider, int rowsPerPage) {
		this(id, columns.toArray(new IColumn[columns.size()]), dataProvider, rowsPerPage);
	}

	public CustomDataTable(String id, final IColumn<T>[] columns, ISortableDataProvider<T> dataProvider, int rowsPerPage) {
		super(id, columns, dataProvider, rowsPerPage);

		addTopToolbar(new NavigationToolbar(this) {
			private static final long serialVersionUID = 834241944644931794L;

			@Override
			protected PagingNavigator newPagingNavigator(String navigatorId, final DataTable<?> table) {
				return new CostumPagingNavigator<Object>(navigatorId, table);
			}

		});
		addTopToolbar(new HeadersToolbar(this, dataProvider));
		addBottomToolbar(new NoRecordsToolbar(this, new StringResourceModel("datatable.no-records-found", null, "No Records Found")));
	}

	public void saveCurrentPage() {
		SessionUtils.setSessionMetaData(CustomDataTableCurrentPageMetaDataKey.KEY, getCurrentPage());
        LOGGER.info("Saving current page to " + getCurrentPage());
	}
	
	public void restoreCurrentPage() {
        Integer currentPage = SessionUtils.getSessionMetaData(CustomDataTableCurrentPageMetaDataKey.KEY);
        if(currentPage != null && currentPage > -1 && currentPage < getPageCount()) {
            setCurrentPage( currentPage );
            LOGGER.info("Setting current page to " + currentPage);
        } else {
            LOGGER.info("No current page in session.");
        }
	}
	
	@Override
	protected Item<T> newRowItem(String id, int index, IModel<T> model) {
		return new OddEvenItem<T>(id, index, model);
	}
}
