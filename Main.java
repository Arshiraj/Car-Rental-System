import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Car {
    private String carId;
    private String brand;
    private String model;
    private double basePricePerDay;
    private boolean isAvailable;

    public Car(String carId, String brand, String model, double basePricePerDay) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.basePricePerDay = basePricePerDay;
        this.isAvailable = true;
    }

    public String getCarId() { return carId; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public double getBasePricePerDay() { return basePricePerDay; }

    public double calculatePrice(int days) {
        return basePricePerDay * days;
    }

    public boolean isAvailable() { return isAvailable; }

    public void rent() { isAvailable = false; }
    public void returnCar() { isAvailable = true; }
}

class Customer {
    private String customerId;
    private String name;

    public Customer(String id, String name) {
        this.customerId = id;
        this.name = name;
    }

    public String getCustomerId() { return customerId; }
    public String getName() { return name; }
}

class Rental {
    private Car car;
    private Customer customer;
    private int days;

    public Rental(Car car, Customer customer, int days) {
        this.car = car;
        this.customer = customer;
        this.days = days;
    }

    public Car getCar() { return car; }
    public Customer getCustomer() { return customer; }
    public int getDays() { return days; }
}

class CarRentalSystem {
    private List<Car> cars = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private List<Rental> rentals = new ArrayList<>();

    public void addCar(Car car) { cars.add(car); }

    public void addCustomer(Customer customer) { customers.add(customer); }

    public void showCars() {
        System.out.println("\n===== Car List =====");
        for (Car car : cars) {
            String status = car.isAvailable() ? "Available" : "Not Available";
            System.out.println(car.getCarId() + " - " + car.getBrand() + " " + car.getModel()
                    + " | Rs." + car.getBasePricePerDay() + "/day | " + status);
        }
    }

    public void rentCar(Scanner sc) {
        System.out.println("\n== Rent a Car ==");

        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        showCars();

        System.out.print("\nEnter Car ID: ");
        String carId = sc.nextLine();

        Car selectedCar = null;
        for (Car car : cars) {
            if (car.getCarId().equalsIgnoreCase(carId)) {
                selectedCar = car;
                break;
            }
        }

        if (selectedCar == null) {
            System.out.println(" Invalid Car ID!");
            return;
        }

        if (!selectedCar.isAvailable()) {
            System.out.println(" Car is already rented!");
            return;
        }

        System.out.print("Enter number of days: ");
        int days = sc.nextInt();
        sc.nextLine();

        if (days <= 0) {
            System.out.println(" Invalid number of days!");
            return;
        }

        Customer customer = new Customer("CUS" + (customers.size() + 1), name);
        addCustomer(customer);

        double total = selectedCar.calculatePrice(days);

        // Receipt
        System.out.println("\n===== RENTAL RECEIPT =====");
        System.out.println("Customer ID: " + customer.getCustomerId());
        System.out.println("Customer Name: " + customer.getName());
        System.out.println("Car: " + selectedCar.getBrand() + " " + selectedCar.getModel());
        System.out.println("Days: " + days);
        System.out.println("Price per day: Rs." + selectedCar.getBasePricePerDay());
        System.out.println("Total: Rs." + total);
        System.out.println("==========================");

        System.out.print("Confirm rental (Y/N): ");
        String confirm = sc.nextLine();

        if (confirm.equalsIgnoreCase("Y")) {
            selectedCar.rent();
            rentals.add(new Rental(selectedCar, customer, days));
            System.out.println(" Car rented successfully!");
        } else {
            System.out.println(" Rental cancelled.");
        }
    }

    public void returnCar(Scanner sc) {
        System.out.println("\n== Return a Car ==");

        System.out.print("Enter Car ID: ");
        String carId = sc.nextLine();

        Car carToReturn = null;
        for (Car car : cars) {
            if (car.getCarId().equalsIgnoreCase(carId) && !car.isAvailable()) {
                carToReturn = car;
                break;
            }
        }

        if (carToReturn == null) {
            System.out.println(" Invalid Car ID or Car not rented!");
            return;
        }

        Rental rentalToRemove = null;
        for (Rental rental : rentals) {
            if (rental.getCar() == carToReturn) {
                rentalToRemove = rental;
                break;
            }
        }

        if (rentalToRemove != null) {
            carToReturn.returnCar();
            rentals.remove(rentalToRemove);
            System.out.println(" Car returned successfully by " + rentalToRemove.getCustomer().getName());
        }
    }

    public void menu() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Car Rental System =====");
            System.out.println("1. Rent a Car");
            System.out.println("2. Return a Car");
            System.out.println("3. Show Cars");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> rentCar(sc);
                case 2 -> returnCar(sc);
                case 3 -> showCars();
                case 4 -> {
                    System.out.println("Thank you for using the system!");
                    return;
                }
                default -> System.out.println(" Invalid choice!");
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        CarRentalSystem system = new CarRentalSystem();

        system.addCar(new Car("C001", "Toyota", "Camry", 3000));
        system.addCar(new Car("C002", "Honda", "Accord", 3500));
        system.addCar(new Car("C003", "Mahindra", "Thar", 6000));

        system.menu();
    }
}