package com.gnomon.epsos.model;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A list of current session stored diagnoses.
 *
 * @author Akis Papadopoulos
 */
@ManagedBean(name = "diagnoses")
@SessionScoped
public class Diagnoses implements Serializable {

    private static final long serialVersionUID = -1751528621432239344L;
    //Current stored diagnoses
    private List<Diagnosis> list = new ArrayList<>();
    //Current selected diagnosis
    private Diagnosis selectedDiagnosis;

    /**
     * A method returning the selected diagnosis.
     *
     * @return the selected diagnosis.
     */
    public Diagnosis getSelectedDiagnosis() {
        return selectedDiagnosis;
    }

    /**
     * A method setting the selected diagnosis.
     *
     * @param diagnosis the selected diagnosis.
     */
    public void setSelectedDiagnosis(Diagnosis diagnosis) {
        this.selectedDiagnosis = diagnosis;
    }

    /**
     * A method adding a diagnosis into the list of diagnoses.
     *
     * @param diagnosis a diagnosis.
     */
    public void add(Diagnosis diagnosis) {
        list.add(diagnosis);
    }

    /**
     * A method returning the list of current stored diagnoses.
     *
     * @return the list of current stored diagnoses.
     */
    public List<Diagnosis> getList() {
        return list;
    }

    /**
     * A method handling a delete action event of a already stored diagnosis
     * within the list of diagnoses.
     *
     * @param event an action event.
     */
    public void delete(ActionEvent event) {
        list.remove(selectedDiagnosis);
    }

    /**
     * A method returning if the diagnoses list is empty.
     *
     * @return if the list of diagnoses is empty.
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * A method cleaning the list of diagnoses.
     */
    public void clear() {
        //Unselecting the curently selected diagnosis
        selectedDiagnosis = null;

        //Getting the list iterator
        Iterator<Diagnosis> it = list.iterator();

        //Iterating through the list of stored diagnoses
        while (it.hasNext()) {
            //Removing the next stored diagnosis
            it.next();
            it.remove();
        }
    }
}
