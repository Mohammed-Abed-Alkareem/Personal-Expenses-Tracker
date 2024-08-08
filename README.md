
# Personal Expenses Tracker

This Android application helps users track their personal finances. The app is designed with a single activity containing three fragments: `AddFragment`, `ListFragment`, and `DetailsFragment`. It utilizes an SQLite database to store expenses, shared preferences for user settings, and follows best practices in Android development.

## Features

- **Add Expenses:** Users can add new expenses, including the type, amount, and optional notes. The current date and time are automatically recorded.
- **View Expenses:** A list of all recorded expenses is displayed with their type and timestamp.
- **Expense Details:** Clicking on an entry provides detailed information, including options to delete the entry or adjust the font size for readability.
- **Persistent Settings:** Font size adjustments are saved using shared preferences, ensuring a consistent experience across sessions.

## User Interface

The application consists of the following fragments:

### AddFragment
- **Dropdown:** Select an expense type from a predefined list.
- **InputNumberField:** Enter the amount of the expense.
- **MultilineTextArea:** Add optional notes.
- **AddButton:** Save the expense, which includes the current date and time.

### ListFragment
- **Displays all expenses** with their type and timestamp.
- **Automatically updates** when new expenses are added.

### DetailsFragment
- **Shows detailed information** about a selected expense.
- **Delete button:** Remove the expense from the database.
- **Font size adjustment:** Increase or decrease the text size, with settings saved in shared preferences.

## Installation

1. Clone this repository:
   ```bash
   git clone https://github.com/yourusername/PersonalExpensesTracker.git
   ```
2. Open the project in Android Studio.
3. Build and run the app on an emulator or Android device.

## Usage

1. Start the app to access the main screen with all three fragments visible.
2. Use the `AddFragment` to input and save new expenses.
3. View all added expenses in the `ListFragment`.
4. Click on any entry to view detailed information in the `DetailsFragment`, where you can delete the entry or adjust the font size.

## Demo

![App Demo](demo.gif)

## Requirements

- Android Studio with API Level 26 or higher.
- Compatible with Pixel 3a XL device.



## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
