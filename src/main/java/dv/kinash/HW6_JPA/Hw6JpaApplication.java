package dv.kinash.HW6_JPA;

import dv.kinash.HW6_JPA.model.User;
import dv.kinash.HW6_JPA.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class Hw6JpaApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(Hw6JpaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		final String consoleCommands = "Here you can:\n" +
				"/list - see user list\n" +
				"/get_by_id <id> - get user by ID\n" +
				"/get_by_email <email> - get user by e-mail\n" +
				"/add - create new user\n" +
				"/delete <id> - delete user by ID\n" +
				"/update <id> <field> <value> - update user property\n" +
				"/help - see this help\n" +
				"/exit - terminate program";

		System.out.println("===  Welcome to User Management System! ===");
		System.out.println(consoleCommands);

		Scanner scanner = new Scanner(System.in);
		do {
			String command = scanner.next();
			switch (command) {
				case "/list" : {
					final List<User> list = userRepository.getList();
					if (list == null || list.size() == 0)
						System.out.println("<empty list>");
					else
						list.stream().forEach(user -> System.out.println(user));
					break;
				}
				case "/get_by_id" : {
					final Long id = scanner.nextLong();
					final User user = userRepository.getById(id);
					if (user == null)
						System.out.println("User with id="+id+" NOT found!");
					else
						System.out.println(user);
					break;
				}
				case "/get_by_email" : {
					final String email = scanner.next();
					final List<User> list = userRepository.getByEmail(email);
					if (list == null || list.size() == 0)
						System.out.println("<empty list>");
					else
						list.stream().forEach(user -> System.out.println(user));
					break;
				}
				case "/add" : {
					scanner.nextLine();
					System.out.print("Name: ");
					final String name = scanner.nextLine();
					System.out.print("E-mail: ");
					final String email = scanner.nextLine();
					User user = new User(name, email);
					user = userRepository.save(user);
					System.out.println("Has been added: " + user);
					break;
				}
				case "/delete" :{
					final Long id = scanner.nextLong();
					if (userRepository.delete(id))
						System.out.println("User with id="+id+" deleted!");
					else
						System.out.println("User with id="+id+" NOT found!");
					break;
				}
				case "/update" : {
					final Long id = scanner.nextLong();
					final String fieldName = scanner.next();
					final String fieldValue = scanner.nextLine();

					User user = userRepository.getById(id);
					if (user == null) {
						System.out.println("User with id="+id+" NOT found!");
						break;
					}
					try {
						Field field = User.class.getDeclaredField(fieldName);
						field.setAccessible(true);
						field.set(user, fieldValue);
					} catch (NoSuchFieldException e) {
						System.out.println("Field "+fieldName+" in User NOT found!");
						break;
					}
					user = userRepository.save(user);
					System.out.println("Has been updated: " + user);
				}
					System.out.println("update");
					break;
				case "/help" :
				case "/?" :
				case "?" :
					System.out.println(consoleCommands);
					break;
				case "/exit" :
					System.exit(0);
					break;
				default :
					System.out.println("unknown command: " + command);
			}

		} while (true);
	}
}
