/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/package se.sb.epsos.web.pages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;

import se.sb.epsos.web.BasePage;
import se.sb.epsos.web.model.Person;

public class TestPage extends BasePage {

    public TestPage(Person person) {
        add(new TestForm("testForm")).setOutputMarkupId(true);
    }

    public class TestForm extends Form<TestFormModel> {
		private static final long serialVersionUID = 8147216706035343297L;
		private DropDownChoice<String> dropdown;
        private ListView<String> labellist;

        public TestForm(String id) {
            super(id,new CompoundPropertyModel<TestFormModel>(new TestFormModel("nothing is selected...")));
            List<String> choices = new ArrayList<String>(Arrays.asList(new String[]{"1", "2", "3", "4"}));
            dropdown = new DropDownChoice<String>("selected", choices);
            dropdown.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				private static final long serialVersionUID = -8664861436410178383L;

				@Override
                protected void onUpdate(AjaxRequestTarget ajaxRequestTarget) {
                    TestFormModel formModel = (TestFormModel)TestForm.this.getDefaultModelObject();
                    int size = Integer.parseInt((String) this.getComponent().getDefaultModelObject());
                    System.out.println("Choosen list size: "+size);
                    List<String> newList = new ArrayList<String>();
                    for (int i=0; i < size; i++) {
                        newList.add("Item #"+i);
                    }
                    formModel.setLabellist(newList);
                    System.out.println("Size is set to: "+formModel.getLabellist().size());
                    ajaxRequestTarget.addComponent(TestForm.this);
                }
            });
            add(dropdown);

            labellist = new ListView<String>("labellist") {
				private static final long serialVersionUID = 5006135995209691176L;

				@Override
                protected void populateItem(ListItem<String> item) {
                    System.out.println("Pupulating list item #"+item.getIndex());
                    item.add(new Label("label","label #"+item.getIndex()));
                    item.add(new TextField<String>("textfield",item.getModel()));
                }
            };
            labellist.setOutputMarkupId(true);
            add(labellist);
        }
    }

    public class TestFormModel implements Serializable {
		private static final long serialVersionUID = 401666643477698331L;
		private String selected;
        private List<String> labellist = new ArrayList<String>();

        public TestFormModel(String selected) {
            this.selected = selected;
        }

        public String getSelected() {
            return selected;
        }

        public void setSelected(String selected) {
            this.selected = selected;
        }

        public List<String> getLabellist() {
            return labellist;
        }

        public void setLabellist(List<String> labellist) {
            this.labellist = labellist;
        }
    }

}
