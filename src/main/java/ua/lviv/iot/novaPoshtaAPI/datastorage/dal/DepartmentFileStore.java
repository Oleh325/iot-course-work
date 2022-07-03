package ua.lviv.iot.novaPoshtaAPI.datastorage.dal;

import org.springframework.stereotype.Component;
import ua.lviv.iot.novaPoshtaAPI.model.Department;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Objects;

@Component
public class DepartmentFileStore extends FileStore<Department> {

    @Override
    protected Long getId(Department department) {
        return department.getDepartmentId();
    }

    @Override
    protected Department fill(List<String> values) {
        List<Long> ids = new LinkedList<>();
        if (!Objects.equals(values.get(3), "")) {
            ids = Arrays.stream(values.get(3).split(", "))
                    .map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        }
        return new Department(Long.parseLong(values.get(0)), values.get(1), values.get(2), ids);
    }

    @Override
    protected String getObjectPrefix() {
        return "department";
    }


}
