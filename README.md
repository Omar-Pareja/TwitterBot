# TwitterBot Assignment

A Markov Chain-based AI for generating tweets. This project parses CSV data, trains a Markov Chain model, and uses it to generate realistic tweets based on the training data.

## Features

- **Markov Chain Model**: Generates tweets using a probabilistic language model.
- **CSV Parsing**: Reads and processes training data from CSV files.
- **File I/O**: Handles input and output for training and generated tweets.
- **Customizable**: Easily train the bot on new datasets.

## Requirements

This project is written in Java. Ensure you have the following installed:

- Java 17 or later
- JUnit 5 for testing

## Files

- `MarkovChain.java`: Core implementation of the Markov Chain model.
- `TwitterBot.java`: Main class for generating tweets.
- `CSVParser.java`: Handles CSV file parsing.
- `TwitterBotTest.java`: Unit tests for the project.

## How to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/twitterbot.git
   ```

2. Compile the project:
   ```bash
   javac *.java
   ```

3. Run the TwitterBot:
   ```bash
   java TwitterBot <training_data.csv> <number_of_tweets>
   ```

   Example:
   ```bash
   java TwitterBot training_data.csv 5
   ```
   This generates 5 tweets based on the `training_data.csv` file.

## Tests

Run the unit tests using JUnit:
```bash
java -jar junit-platform-console-standalone-1.8.2.jar --class-path . --scan-class-path
```

## How It Works

1. **Training**: The bot reads a CSV file containing tweet data and builds a Markov Chain.
2. **Generation**: Using the Markov Chain, it constructs tweets one word at a time, based on probabilities derived from the training data.
3. **Output**: The generated tweets are printed to the console or saved to a file.

## Example Output

With training data from a dataset of tweets, the bot may generate:
```
"I love coding and coffee in the morning!"
"Learning Java is so much fun with Markov Chains."
```

## Contributing

Feel free to fork this repository and make improvements! Submit a pull request with your changes.

## License

This project is for educational purposes only and is not intended for production use. No license is provided.
