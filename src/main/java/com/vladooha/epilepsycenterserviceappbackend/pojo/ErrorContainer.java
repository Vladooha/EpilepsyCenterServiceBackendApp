package com.vladooha.epilepsycenterserviceappbackend.pojo;

import java.util.ArrayList;
import java.util.List;

public class ErrorContainer {
    private List<String> errorList;

    public ErrorContainer(String... errors) {
        errorList = new ArrayList<>();

        addErrors(errors);
    }

    public void addErrors(String... errors) {
        for (String error : errors) {
            errorList.add(error);
        }
    }

    public List<String> getErrors() {
        return new ArrayList<>(errorList);
    }

    public boolean hasErrors() {
        return !errorList.isEmpty();
    }

    public void concat(ErrorContainer errorContainer) {
        addErrors(errorContainer.errorList.toArray(new String[0]));
    }
}
