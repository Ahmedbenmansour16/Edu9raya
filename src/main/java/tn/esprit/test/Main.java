package tn.esprit.test;

<<<<<<< HEAD
import tn.esprit.models.Cour;
import tn.esprit.models.Module;
import tn.esprit.services.CourService;
import tn.esprit.services.ModuleService;

import java.sql.Timestamp;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Initialize CourService
        CourService courService = new CourService();

        // Create a Module instance with id = 15 (assuming it exists in the database)
        Module module = new Module();
        module.setId(15);

        // 1. Test Create (Add a new course)
        Cour newCourse = new Cour(
                "CS101",
                "Introduction to Programming",
                "Beginner",
                "Programming",
                module,
                "This course covers the basics of programming.",
                "/path/to/pdf/course.pdf"
        );
        System.out.println("Adding a new course...");
        courService.add(newCourse);

        // 2. Test Read (Retrieve all courses)
        System.out.println("\nRetrieving all courses...");
        List<Cour> allCourses = courService.retrieveAll();
        for (Cour course : allCourses) {
            System.out.println(course);
        }

        // 3. Test Read by Module ID (Retrieve courses for module_id = 15)
        System.out.println("\nRetrieving courses for module ID 15...");
        List<Cour> moduleCourses = courService.retrieveByModuleId(15);
        for (Cour course : moduleCourses) {
            System.out.println(course);
        }

        // 4. Test Update (Update the first course from the retrieved list, if any)
        if (!allCourses.isEmpty()) {
            Cour courseToUpdate = allCourses.get(0);
            courseToUpdate.setTitre("Updated Introduction to Programming");
            courseToUpdate.setDescription("Updated description for the programming course.");
            courseToUpdate.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            System.out.println("\nUpdating course with ID " + courseToUpdate.getId() + "...");
            courService.update(courseToUpdate);

            // Verify update
            System.out.println("Retrieving updated course...");
            allCourses = courService.retrieveAll();
            for (Cour course : allCourses) {
                if (course.getId() == courseToUpdate.getId()) {
                    System.out.println(course);
                }
            }
        }

        // 5. Test Delete (Delete the first course from the retrieved list, if any)
        if (!allCourses.isEmpty()) {
            int courseIdToDelete = allCourses.get(0).getId();
            System.out.println("\nDeleting course with ID " + courseIdToDelete + "...");
            courService.delete(courseIdToDelete);

            // Verify deletion
            System.out.println("Retrieving all courses after deletion...");
            allCourses = courService.retrieveAll();
            for (Cour course : allCourses) {
                System.out.println(course);
            }
        }
=======
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

>>>>>>> 3995ed6bc298f7141ffba057c47f0d040947cff1
    }
}