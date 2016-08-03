import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Scanner;
import by.itclass.common.PhoneNote;
import by.itclass.common.PhoneNumber;
import by.itclass.common.PhoneType;
import by.itclass.common.TagType;
import by.itclass.daos.IPhoneNotesDAO;
import by.itclass.daos.PhoneNotesMemory;

public class Application {
	
	static int id = 1;

	private static void showMenu() {
		System.out.println("Выберите действие");
		System.out.println("1. Добавить запись");
		System.out.println("2. Удалить запись");
		System.out.println("3. Изменить запись");
		System.out.println("4. Показать список");
		System.out.println("5. Отсортировать по номерам");
		System.out.println("6. Отсортировать по именам");
		System.out.println("7. Отсортировать по тегам");
		System.out.println("8. Выход");
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String command = "";
		int commandnumber = 0;
		
		IPhoneNotesDAO dao = new PhoneNotesMemory();
		
		do {
			showMenu();
			command = sc.nextLine();
			try {
				commandnumber = Integer.parseInt(command);
			} catch (NumberFormatException e) {
				commandnumber = 0;
			}
			switch (commandnumber) {
			case 1:
				addNote(dao, sc);
				break;
			case 2:
				deleteNote(dao, sc);
				break;
			case 3:
				updateNote(dao, sc);
				break;
			case 4:
				showNotes(dao);
				break;
			case 5:
				sortNotesByNumber(dao);
				break;
			case 6:
				sortNotesByNames(dao);
				break;
			case 7:
				sortNotesByTags(dao);
				break;
			default:
				break;
				}
			clearConsole();
		} while (commandnumber != 8);
		sc.close();
	}

	private static void sortNotesByTags(IPhoneNotesDAO dao) {
		dao.Sort(new Comparator<PhoneNote>() {
			
			@Override
			public int compare(PhoneNote arg0, PhoneNote arg1) {
				return arg0.getTag().compareTo(arg1.getTag());
			}
		});
		
	}

	private static void sortNotesByNames(IPhoneNotesDAO dao) {
		dao.Sort(new Comparator<PhoneNote>() {
			
			@Override
			public int compare(PhoneNote o1, PhoneNote o2) {
				int comp = o1.getSurname().compareTo(o2.getSurname());
				if (comp == 0) {
					return o1.getName().compareTo(o2.getName());
				}
				else {
					return comp;
				}
			}
		});
	}

	private static void sortNotesByNumber(IPhoneNotesDAO dao) {
		dao.Sort(new Comparator<PhoneNote>() {
			
			@Override
			public int compare(PhoneNote o1, PhoneNote o2) {
				
				PhoneNumber n1 = o1.getNumber();
				PhoneNumber n2 = o2.getNumber();
				int cmp = n1.getNumber().compareTo(n2.getNumber());
				if (cmp == 0) {
					return n1.getType().compareTo(n2.getType());
				} else {
					return cmp;
				}
			}
		});
	}

	private static void showNotes(IPhoneNotesDAO dao) {
		PhoneNote[] notes = dao.AllNotes();
		for (PhoneNote phoneNote : notes) {
			PhoneNumber number = phoneNote.getNumber();
			String out = String.format("%s, %s, %s, %s, %s", number.getNumber(), number.getType(), phoneNote.getSurname(), phoneNote.getName(), phoneNote.getTag());
			System.out.println(out);
		}
	}

	private static void updateNote(IPhoneNotesDAO dao, Scanner sc) {
		int id = 0;
		String ids = null;
		boolean ok = true;
		do 
		{
			System.out.println("Введите id-записи");
			try {
				ids = sc.nextLine();
				if (ids == null) {
					ok = false;
				} else {
					id = Integer.parseInt(ids);
					ok = true;
				}
			} catch (NoSuchElementException e) {
				ok = false;
			} catch (NumberFormatException e) {
				ok = false;
			}
		} while (!ok);
		

		PhoneNote note = dao.FindByID(id);
		
		int point = 0;
		String points = null;
		do 
		{
			System.out.println("Что вы хотите изменить?");
			System.out.println("1. Номер");
			System.out.println("2. Тип");
			System.out.println("3. Имя");
			System.out.println("4. Фамилия");
			System.out.println("5. Тег");
			System.out.println("6. Ничего");
			try {
				points = sc.nextLine();
				if (points == null) {
					ok = false;
				} else {
					point = Integer.parseInt(points);
					ok = true;
				}
			} catch (NoSuchElementException e) {
				ok = false;
			} catch (NumberFormatException e) {
				ok = false;
			}
		} while (!ok);
		
		switch (point) {
		case 1:
			String number = null;
			do 
			{
				System.out.println("Введите номер");
				try {
					number = sc.nextLine();
					if (number == null) {
						ok = false;
					} else {
						ok = true;
					}
				} catch (NoSuchElementException e) {
					ok = false;
				}
			} while (!ok);
			PhoneNumber numb = note.getNumber();
			numb.setNumber(number);
			note.setNumber(numb);
			dao.Update(note);
			break;
		case 2:
			String stype = null;
			PhoneType type = null;
			do
			{
				System.out.println("Введите тип номера");
				System.out.println("1. Домашний");
				System.out.println("2. Рабочий");
				System.out.println("3. Офис");
				try {
					stype = sc.nextLine();
					if (stype == null) {
						ok = false;
					} else {
						int n = Integer.parseInt(stype);
						switch (n) {
						case 1:
							type = PhoneType.HOME;
							ok = true;
							break;
						case 2:
							type = PhoneType.WORK;
							ok = true;
							break;
						case 3:
							type = PhoneType.OFFICE;
							ok = true;
							break;
						default:
							ok = false;
							break;
						}
					}
				} catch (NoSuchElementException e) {
					ok = false;
				} catch (NumberFormatException e) {
					ok = false;
				}
			} while (!ok);
			PhoneNumber numb1 = note.getNumber();
			numb1.setType(type);
			note.setNumber(numb1);
			dao.Update(note);
			break;
		case 3:
			String name = null;
			do 
			{
				System.out.println("Введите имя");
				try {
					name = sc.nextLine();
					if (name == null) {
						ok = false;
					} else {
						ok = true;
					}
				} catch (NoSuchElementException e) {
					ok = false;
				}
			} while (!ok);
			note.setName(name);
			dao.Update(note);
			break;
		case 4:
			String surname = null;
			do 
			{
				System.out.println("Введите фамилию");
				try {
					surname = sc.nextLine();
					if (surname == null) {
						ok = false;
					} else {
						ok = true;
					}
				} catch (NoSuchElementException e) {
					ok = false;
				}
			} while (!ok);
			note.setName(surname);
			dao.Update(note);
			break;
		case 5:
			String stag = null;
			TagType tag = null;
			do
			{
				System.out.println("Введите тег");
				System.out.println("1. Семья");
				System.out.println("2. Коллега");
				System.out.println("3. Друг");
				try {
					stag = sc.nextLine();
					if (stag == null) {
						ok = false;
					} else {
						int n = Integer.parseInt(stag);
						switch (n) {
						case 1:
							tag = TagType.FAMILY;
							ok = true;
							break;
						case 2:
							tag = TagType.COLLEAGUE;
							ok = true;
							break;
						case 3:
							tag = TagType.FRIEND;
							ok = true;
							break;
						default:
							ok = false;
							break;
						}
					}
				} catch (NoSuchElementException e) {
					ok = false;
				} catch (NumberFormatException e) {
					ok = false;
				}
			} while (!ok);
			note.setTag(tag);
			dao.Update(note);
			break;
		}
		
		
	}

	private static void deleteNote(IPhoneNotesDAO dao, Scanner sc) {
		int id = 0;
		String ids = null;
		boolean ok = true;
		do 
		{
			System.out.println("Введите id-записи");
			try {
				ids = sc.nextLine();
				if (ids == null) {
					ok = false;
				} else {
					id = Integer.parseInt(ids);
					ok = true;
				}
			} catch (NoSuchElementException e) {
				ok = false;
			} catch (NumberFormatException e) {
				ok = false;
			}
		} while (!ok);
		
		PhoneNote note = dao.FindByID(id);
		dao.Delete(note);		
	}

	private static void addNote(IPhoneNotesDAO dao, Scanner sc) {
		String name = null, surname = null, number = null, stag, stype;
		TagType tag = null;
		PhoneType type = null;
		boolean ok = true;
		do 
		{
			System.out.println("Введите имя");
			try {
				name = sc.nextLine();
				if (name == null) {
					ok = false;
				} else {
					ok = true;
				}
			} catch (NoSuchElementException e) {
				ok = false;
			}
		} while (!ok);
		do 
		{
			System.out.println("Введите фамилию");
			try {
				surname = sc.nextLine();
				if (surname == null) {
					ok = false;
				} else {
					ok = true;
				}
			} catch (NoSuchElementException e) {
				ok = false;
			}
		} while (!ok);
		do
		{
			System.out.println("Введите тег");
			System.out.println("1. Семья");
			System.out.println("2. Коллега");
			System.out.println("3. Друг");
			try {
				stag = sc.nextLine();
				if (stag == null) {
					ok = false;
				} else {
					int n = Integer.parseInt(stag);
					switch (n) {
					case 1:
						tag = TagType.FAMILY;
						ok = true;
						break;
					case 2:
						tag = TagType.COLLEAGUE;
						ok = true;
						break;
					case 3:
						tag = TagType.FRIEND;
						ok = true;
						break;
					default:
						ok = false;
						break;
					}
				}
			} catch (NoSuchElementException e) {
				ok = false;
			} catch (NumberFormatException e) {
				ok = false;
			}
			sc.reset();
		} while (!ok);
		do 
		{
			System.out.println("Введите номер");
			try {
				number = sc.nextLine();
				if (number == null) {
					ok = false;
				} else {
					ok = true;
				}
			} catch (NoSuchElementException e) {
				ok = false;
			}
		} while (!ok);
		do
		{
			System.out.println("Введите тип номера");
			System.out.println("1. Домашний");
			System.out.println("2. Рабочий");
			System.out.println("3. Офис");
			try {
				stype = sc.nextLine();
				if (stype == null) {
					ok = false;
				} else {
					int n = Integer.parseInt(stype);
					switch (n) {
					case 1:
						type = PhoneType.HOME;
						ok = true;
						break;
					case 2:
						type = PhoneType.WORK;
						ok = true;
						break;
					case 3:
						type = PhoneType.OFFICE;
						ok = true;
						break;
					default:
						ok = false;
						break;
					}
				}
			} catch (NoSuchElementException e) {
				ok = false;
			} catch (NumberFormatException e) {
				ok = false;
			}
			sc.reset();
		} while (!ok);
		
		PhoneNumber numb = new PhoneNumber(number, type);
		PhoneNote note = new PhoneNote(id, name, surname, numb, tag);
		id++;
		
		dao.Add(note);
		
	}
	
	private static void clearConsole() {
		final String operatingSystem = System.getProperty("os.name");

		try {
		if (operatingSystem .contains("Windows")) {
		    Runtime.getRuntime().exec("cls");
		}
		else {
		    Runtime.getRuntime().exec("clear");
		}
		} catch (Exception e) {
			
		}
	}

}
