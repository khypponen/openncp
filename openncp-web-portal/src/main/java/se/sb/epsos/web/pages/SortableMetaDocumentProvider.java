/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.sb.epsos.web.pages;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import se.sb.epsos.web.model.LoadableDocumentModel;
import se.sb.epsos.web.service.MetaDocument;

/**
 *
 * @author andreas
 */
class SortableMetaDocumentProvider extends SortableDataProvider<MetaDocument> {
	private static final long serialVersionUID = -7045270911557629188L;
	private List<LoadableDocumentModel<MetaDocument>> documentList;
	private SortableDataProviderComparator comparator = new SortableDataProviderComparator();
	
    public SortableMetaDocumentProvider(List<LoadableDocumentModel<MetaDocument>> wrappedDocumentList) {
        this.documentList = wrappedDocumentList;
    }

    public Iterator<MetaDocument> iterator(int start, int count) {
        if (documentList == null || documentList.isEmpty()) {
            return Collections.<MetaDocument>emptyList().iterator();
        } else {
            Collections.sort(documentList, comparator);
            List<MetaDocument> newList = new ArrayList<MetaDocument>();
            for (LoadableDocumentModel<MetaDocument> doc : documentList.subList(start, start + count)) {
                newList.add(doc.getObject());
            }
            
            return newList.iterator();
        }
    }

    public int size() {
        return documentList != null ? documentList.size() : 0;
    }

    public IModel<MetaDocument> model(MetaDocument doc) {
        for (LoadableDocumentModel<MetaDocument> model : this.documentList) {
            if (model != null && model.getObject() != null && model.getObject().equals(doc)) {
                return model;
            }
        }
        return null;
    }

    private class SortableDataProviderComparator implements Comparator<LoadableDocumentModel<MetaDocument>>, Serializable {
		private static final long serialVersionUID = 1404397511808983698L;
		private transient Collator collator;

		public SortableDataProviderComparator() {
			super();
			init();
		}
		
		private void init() {
	        collator = Collator.getInstance();
	        collator.setStrength(Collator.PRIMARY);
		}
		
		public int compare(final LoadableDocumentModel<MetaDocument> o1, final LoadableDocumentModel<MetaDocument> o2) {
			PropertyModel<Comparable<String>> model1 = new PropertyModel<Comparable<String>>(o1, getSort().getProperty());
			PropertyModel<Comparable<String>> model2 = new PropertyModel<Comparable<String>>(o2, getSort().getProperty());

			if(model1 == null || model1.getObject() == null || model2 == null || model2.getObject() == null) {
				return 0;
			} else {
				int result = collator.compare(model1.getObject(), model2.getObject());

				return getSort().isAscending() ? result : -result;
			}
		}
		
		private void writeObject(java.io.ObjectOutputStream out) throws IOException {
			out.defaultWriteObject();
		}

		private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
			in.defaultReadObject();
			init();
		}

		@SuppressWarnings("unused")
		private void readObjectNoData() throws ObjectStreamException {
			init();
		}
	}
}
