package program.entity.users;

import program.control.security.Password;

/**
 * <p>
 * The {@code Applicant} class represents a specific type of user, an applicant, in the system.
 * It extends the {@link User} class and inherits the general user properties and functionality,
 * such as NRIC, name, age, marital status, and password. The {@code Applicant} class provides
 * constructors to initialize these properties for a new applicant.
 * </p>
 *
 * <p>
 * The class is designed to be lightweight as it currently does not add new functionality beyond
 * what is provided by its superclass {@link User}. However, it is intended to be a placeholder for
 * future extensions or specific applicant-related logic, should it be required.
 * </p>
 *
 * @see program.entity.users.User
 */
public class Applicant extends User {

    /**
     * Constructs an {@code Applicant} with the specified NRIC, name, age, and marital status.
     * The password is not set in this constructor.
     *
     * @param NRIC the NRIC of the applicant
     * @param name the name of the applicant
     * @param age the age of the applicant
     * @param marital_status the marital status of the applicant
     * @throws Exception if there is an error during the initialization of the applicant
     */
    public Applicant(String NRIC, String name, int age, String marital_status) throws Exception {
        super(NRIC, name, age, marital_status);
    }

    /**
     * Constructs an {@code Applicant} with the specified NRIC, name, age, marital status, and password.
     *
     * @param NRIC the NRIC of the applicant
     * @param name the name of the applicant
     * @param age the age of the applicant
     * @param marital_status the marital status of the applicant
     * @param password the password of the applicant
     * @throws Exception if there is an error during the initialization of the applicant
     */
    public Applicant(String NRIC, String name, int age, String marital_status, String password) throws Exception {
        super(NRIC, name, age, marital_status, password);
    }

    /**
     * Constructs an {@code Applicant} with the specified NRIC, name, age, marital status, and password object.
     *
     * @param NRIC the NRIC of the applicant
     * @param name the name of the applicant
     * @param age the age of the applicant
     * @param marital_status the marital status of the applicant
     * @param password the password object of the applicant
     * @throws Exception if there is an error during the initialization of the applicant
     */
    public Applicant(String NRIC, String name, int age, String marital_status, Password password) throws Exception {
        super(NRIC, name, age, marital_status, password);
    }
}
