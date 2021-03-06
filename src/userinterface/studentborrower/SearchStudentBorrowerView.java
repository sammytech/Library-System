package userinterface.studentborrower;

import Utilities.Utilities;
import com.jfoenix.controls.JFXTextField;
import impresario.IModel;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import model.StudentBorrower;
import userinterface.SearchView;


import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;

/**
 * Created by Sammytech on 3/11/17.
 */
public class SearchStudentBorrowerView extends SearchView<StudentBorrower.DATABASE> {

    JFXTextField firstName;
    JFXTextField lastName;
    JFXTextField phone;
    JFXTextField email;
    private static final int MAX_COLUMN = 2;

    public SearchStudentBorrowerView(IModel model) {
        super(model, "SearchStudentBorrowerView");

    }

    protected Properties validateSearch(){
        Properties search = new Properties();
        String firstNameStr = firstName.getText();
        String lastNameStr = lastName.getText();
        String phoneStr = phone.getText();
        String emailStr = email.getText();
        if(firstNameStr.trim().isEmpty() && lastNameStr.trim().isEmpty() && phoneStr.trim().isEmpty() && emailStr.trim().isEmpty()){
//Error
        } else{
            if(!firstNameStr.trim().isEmpty()){
                search.setProperty(StudentBorrower.DATABASE.FirstName.name(), firstNameStr);
            }
            if(!lastNameStr.trim().isEmpty()){
                search.setProperty(StudentBorrower.DATABASE.LastName.name(), lastNameStr);
            }
            if(!phoneStr.trim().isEmpty() && Utilities.validatePhoneNumber(phoneStr)){
                search.setProperty(StudentBorrower.DATABASE.Phone.name(), phoneStr);
            }
            if(!emailStr.trim().isEmpty()){
                search.setProperty(StudentBorrower.DATABASE.Email.name(), emailStr);
            }
        }
        return search;
    }

    @Override
    protected HashMap<StudentBorrower.DATABASE, String> getFields() {
        return StudentBorrower.getFields();
    }

    @Override
    protected ListView getSearchResult() {
        ListView<StudentBorrowerTableModel> listView = new ListView<>();
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView.setCellFactory(new Callback<ListView<StudentBorrowerTableModel>, ListCell<StudentBorrowerTableModel>>() {
            @Override
            public ListCell<StudentBorrowerTableModel> call(ListView<StudentBorrowerTableModel> param) {
                return new StudentBorrowerListViewCell();
            }
        });
        tableData = FXCollections.observableArrayList();
        listView.prefHeightProperty().bind(Bindings.size(tableData).multiply(StudentBorrowerListViewCell.getCustomHeight()+ 10));
        return listView;
    }

    @Override
    protected void insertDataToTable(Enumeration entries) {

        while (entries.hasMoreElements())
        {
            StudentBorrower nextBook = (StudentBorrower) entries.nextElement();
            Vector<String> view = nextBook.getEntryListView();

            // add this list entry to the list
            StudentBorrowerTableModel nextRowData = new StudentBorrowerTableModel(view);
            tableData.add(nextRowData);

        }
    }

    @Override
    protected int getMaxColumn() {
        return MAX_COLUMN;
    }

    @Override
    protected GridPane createSearch() {
        firstName = new JFXTextField();
        lastName = new JFXTextField();
        phone = new JFXTextField();
        email = new JFXTextField();

        GridPane grid = new GridPane();

        int line = 0;
        int row = line / MAX_COLUMN;
        int col = line % MAX_COLUMN;
        String firstNameStr = fields.get(StudentBorrower.DATABASE.FirstName);
        setupField(firstName, firstNameStr);
        grid.add(firstName, col, row,1,1);

        line++;
        row = line / MAX_COLUMN;
        col = line % MAX_COLUMN;
        String lastNameStr = fields.get(StudentBorrower.DATABASE.LastName);
        setupField(lastName, lastNameStr);
        grid.add(lastName, col, row,1,1);

        line++;
        row = line / MAX_COLUMN;
        col = line % MAX_COLUMN;
        String phoneStr = fields.get(StudentBorrower.DATABASE.Phone);
        setupField(phone, phoneStr);
        grid.add(phone, col, row);

        line++;
        row = line / MAX_COLUMN;
        col = line % MAX_COLUMN;
        String emailStr = fields.get(StudentBorrower.DATABASE.Email);
        setupField(email, emailStr);
        grid.add(email, col, row);

        return grid;
    }

}
