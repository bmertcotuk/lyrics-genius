# Lyrics Genius

This Java Swing (GUI) application aims to ease the process of writing song lyrics. It lets the user to find rhymes for the word they entered using the parameters provided. These parameters include matching vowel/consonanat groups, match direction, and match length. Java Regular Expressions have been used. In the implementation of the graphical user interface of the application the C.R.A.P. (Contrast - Repetition - Alignment - Proximity) design principles have been followed.

## Getting Started

The information pane on the upper right corner helps you navigate over the controls. You can use the shortcuts when typing the lyrics into the text area.

You can save your lyrics to a file and also load lyrics from a file to text area.

The rhyme engine is the core class of the application. Using regular expressions it partialises the input word into successive vowel and consonant groups. Then it returns the words rhyming with the input word according to the parameters provided.

This rhyme engine does not use the phonetic alphabet. However, it can provide many words rhyming with the input word. The application yields the best results in Turkish, since almost each word is pronounced the way it is read.

### Prerequisites

You need to have Maven installed in order to build the application.
You need to have Java 9 installed on your machine with a GUI.

### Installing

If Java and Maven are both installed, you can run the application using the steps below.

To build the application:
```
# in the project's root directory
mvn clean install
```

To run the application:
```
# in the directory where the jar file is located
java -jar LyricsGenius.jar
```

The rhyme engine directly uses regular expressions. Partialisation of a word is concept to demonstrate how the algorithm works. For instance, if the user typed the word 'contemplate', the word will be partialised as c-o-nt-e-mpl-a-t-e. The consonant groups are [c,nt,mpl,t] and the vowel groups are [o,e,a,e].

Now the user can match vowel groups, consonant groups, or both with a given length of groups. If they choose 3 as length and only vowels, for insance, the program will return rhyming words including [moderate,operate,coverage,lowercase,tolerate]. The same logic also applies for the case when only consonants are desired to be matched. However, if the user wants to match both groups with length 3 the program will return rhyming words only ending with "-ate" including [moderate,operate,tolerate].

The user can also specify the direction of the match. In other words, they can match the vowel/consonant groups of given length starting from the beginning (head) of the word or from the end (tail) of the word. Let us consider the case when user wants to match only vowel groups of length 2 starting from the beginning of the word. Here, the rhyming words will also include [psychotherapy,overview,sober].


## Deployment

You can just run the jar file where all properties and input files are provided.

## Built With

* [Java](https://www.java.com) - The language used
* [Maven](https://maven.apache.org/) - Dependency Management

## Contributing

Please feel free to contribute the project by creating a pull request.

## Authors

* **Mert Çotuk** - *Initial work* - [bmertcotuk](https://github.com/bmertcotuk)

## License

This project is licensed under the GNU GPLv3.


