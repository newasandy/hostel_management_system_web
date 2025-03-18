package views;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ManagedBean
@ViewScoped
public class DynamicTableBean implements Serializable {

    private String searchTerm;
    private List<Car> cars;
    private List<Car> filteredCars;
    @PostConstruct
    public void init() {
        cars = new ArrayList<>();
        // Add some sample data
        cars.add(new Car("Toyota", "Camry", 2020));
        cars.add(new Car("Honda", "Accord", 2019));
        cars.add(new Car("Ford", "Mustang", 2021));
        cars.add(new Car("Toyota", "Camry", 2020));
        cars.add(new Car("Honda", "Accord", 2019));
        cars.add(new Car("Ford", "Mustang", 2021));
        cars.add(new Car("Tesla", "Model S", 2022));
        cars.add(new Car("Chevrolet", "Malibu", 2018));
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public List<Car> getCars() {
        return cars;
    }
    public List<Car> getFilteredCars() {
        return filteredCars;
    }

    public void setFilteredCars(List<Car> filteredCars) {
        this.filteredCars = filteredCars;
    }

    public void searchItem(){
        if (searchTerm == null){
            filteredCars = new ArrayList<>(cars);
        }else {
            String lowerString = searchTerm.toLowerCase();
            filteredCars = cars.stream()
                    .filter(car -> car.getMake().toLowerCase().contains(lowerString))
                    .collect(Collectors.toList());
        }
    }

    public static class Car {
        private String make;
        private String model;
        private int year;

        public Car(String make, String model, int year) {
            this.make = make;
            this.model = model;
            this.year = year;
        }

        // Getters and Setters
        public String getMake() {
            return make;
        }

        public void setMake(String make) {
            this.make = make;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }
    }
}
