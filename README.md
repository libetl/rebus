#About the Rebus Generator

##Description
I made it in Java. It is one of the most industrial and proven value language in the IT domain. 
This application uses SWT to run. It is therefore widely runnable in every environment supporting Java (Linux,  MacOS, windows).
How to use it
Let's start the program :

1. Go to the main directory, then in the exe folder
2. Type « java -version » and ensure the value is 1.5 or higher
3. Type « java -jar rebus.jar ». 
4. Write a sentence in the text field then click on the button « Put my life in pictures ! »
5. Below are three results : the rebus in text mode, with drawings and the total cost.
6. You can change the efficiency by adjusting the settings : maximal cost for a word, complexity of the search and length of the found words. It will make the result shorter or cheaper, but also the process longer.

##How does it work ?
The algorithm uses a common structure in Artificial Intelligence software : the Finite State Machine. 
It is the represention of a choice to make among several, working with paths and transitions. The algorithms needs to put a value on each possible track. Then it chooses the best one.

Herein is a simplified diagram of the structure used in the program :

Doing an example step by step could be useful to explain the process . For example : on the word « this »

We submit to the FSM the path « +this ». Thus, we need a node with most of the letters contained in the word.

1. In the FSM, the algorithm found a quite good path : – a + stih ». There is one substraction matching this node  « :ostrich -:orca ».
2. The algorithm states that « this =:ostrich -:orca + a ». It will now find a match for the remaining +a path.
3. There are several matches for the path « -a ». The algorithm takes one randomly : «:ocean  - :cone ».
4. The algorithm give back the answer : « this =:ostrich -:orca + (:ocean -:cone).
5. The algorithm computes the cost. Total cost for this word : 0. But in the settings you can set a maximal value of cost if you want the rebus to be shorter.


##Complexity of the algorithms
Indexing the data has a quadratic complexity (because it computes a path between each pair of words).
org.toilelibre.libe.rebus.init : Method « differencesToFSM »

```java
    for (final Object word1S : imagesSet) {
      for (final Object word2S : imagesSet) {
        if (!word1S.equals (word2S)) {
          final Word word1 = new Word ((String) word1S);
          final Word word2 = new Word ((String) word2S);
          // These words come from the dictionary.
          // So they are keywords (there will be a ':' )
          word1.setKeyword (true);
          word2.setKeyword (true);
          // We add the pair
          WordsFinder.addPath (fsm, word1, word2);
        }
```

However, the search process is exponential by nature. In each step of the process, every transition is scanned. In the worst case, every transition of the FSM is visited. Let's look at 
org.toilelibre.libe.rebus.process.business.WordsFinder

```java
  private static Map<String, Set<Pair<Word, Word>>> findNearestPathsData (...) {
    
    ...

    //We check if there are pairs and if the length of
    //the path fits
    if ((fsm.getData () != null)
        && (pathLength >= (currentLength - lengthDelta))
        && (pathLength <= (currentLength + lengthDelta))) {
      //Good ! Let's add every pair in our map. We copy the set.
    ...
    }
    //Now time for recursion if there are transitions to explore
    if (fsm.getTransitions () != null) {
      for (final String s : fsm.getTransitions ().keySet ()) {
        //For each transition in this state of the FSM
        
        if (...) {
          //We found a matching positive transition in the path
          result.putAll (findNearestPathsData ();
        } else if (...) {
          //We found a matching negative transition in the path
          result.putAll (findNearestPathsData ();
        } else if (...) {
          //We can use the tolerance as a joker for a negative transition
          result.putAll (findNearestPathsData ();
        } else if (...) {
          //We can use the tolerance as a joker for a positive transition
          result.putAll (findNearestPathsData ();
        }
      }
    }
```

II tried several other algorithms. You will find them in the draft folder. One of them is FindBestWordsCombination.java. 

The algorithm tries each combination of words with a given number of words to produce in the rebus. 

It uses both symbols + and – to combine. For each combination of word, we have C(2, k) combinations of symbols.

If k is the number of words to combine, the complexity is O (C(2, k) * n^k).

It sure is a polynomial solution. Unfortunately, for our small amount of data, it is by far slower than our exponential solution.

I also tried an algorithm to find the best similar word to the one submitted. This exponential algorithm uses a dictionary to order the words. This dictionary is a tree where a boolean indicate on each node if there is a end of word. In our scale, each execution is very fast. 

The main advantage of this algorithm is that the found word tries to respect as much as possible the order of letters. It can be tested by launching the draft application (The code can be found in org.toilelibre.libe.rebus.Tests in the draft folder)

I gave it also a naïve try by doing a « for » loop on a List<Word> dictionary. A tip is to compare the likeness of two words by sorting the letters in alphabetical orders (it is often used to compare homonymous words in O(n). Instead of comparing by equals, I made a « distance » method which computes how many letters are different in the words, The method « distance » uses the method « difference » which display in a mathematical form the delta between the words. In the draft folder, the source code can be found in 
org.toilelibre.libe.rebus.objects.Word). The method is used also in my final version. It can be found in  org.toilelibre.libe.rebus.process.business.WordsDifference (I never want to keep functional code in a data object).
Some rebus...

###Lowest Cost
I took the three ones in the test case provided on the itasoftware website. I tried each one with the lowest possible cost and the maximal search potential. (Max cost if possible : 0, complexity 20, length delta 20)

- Is your solution optimal?

```
Is
:stack - :tack + (:rhino - :horn)

your
:radio - :idea + (:eyechart - :chart + (:mug - :gem))

solution
:toadstool - :toad + (:saturn - :star + (:rhino - :horn)

optimal
:terminal -:run + (:soup - :sea + (:game - :gem))

```
Cost : 0

- Outlying data points violated our weakest heuristics.

```
Outlying 
:toungue - :eye + (:globe - :bug + (:billy - :ball + (:cake - :coke + y)))

data 
:teddy - :die + (:karate - :turkey + (:rhino - :horn + (:house - :shoe)))

points 
:present - :ear + (:mail - :mule + (:boat - :bat + (:house - :shoe)))

violated 
:travel - :ear + (:lemonade - :melon + (:orca - :car + (:rhino - :horn)))

our 
:radio - :idea + (:cane - :can + (:house - :shoe))

weakest 
:wastebasket - :bat + (:fire - :fries)

heuristics
:stethoscope - :tape + (:rubik - :book + (:rhino - :horn + (:ocean -:cone)))

```
Cost : 1

- The quiet oenophile adored rhododendrons but loathed zoological exoticness.

```
the
:elephant -:plane

quiet
:tetris -:star + (:game -:gem + (:house -:shoe + q))

oenophile
:dolphin - :die + (:iceskate -:stack + (:ocean - :can))

adored
:teddybear - :bite + (:rain - :yarn + (:orca - :car))

rhododendrons
:rhinoceros - :ice + (:donkey - :key + (:diamond - :man + (:seal - :sail + (:car - :orca))))

but
:thumb - :home + (:ocean - :can)

loathed
:elephant - :open + (:dice - :ice + (:orca - :car + (:boat - :bat)))

zoological
:seagull - :sea + (:magic - :mug + (:snooze - :news + (:bowtie - :bite)))

exoticness
:princess - :pear + (:teeth - :hot + (:mailbox - :lamb + (:boat - :bite + (:cane - :can))))

```
Cost : 5

###Shorter rebus
Allowing the max cost to a higher value can shorten the size of our rebus. Let's try 6 by word (a vowel + a consonant, or 6 vowels can remain in the result for each word) 

- Is your solution optimal?

```
Is
:stack - :tack + i

your
:radio - :idea + yue

solution
:toadstool - :toad + (:saturn - :star + i)

optimal
:terminal -:run + (:soup - :sea + a)

```
Cost : 6

- Outlying data points violated our weakest heuristics.

```
Outlying 
:toungue - :eye + (:globe - :bug -o + yiy)

data 
:teddy - :die – y + aai

points 
:present - :ear -e + oia

violated 
:travel - :ear + (:lemonade - :melon + io)

our 
:radio - :idea + ue

weakest 
:wastebasket - :bat -s

heuristics
:stethoscope - :tape + (:rubik - :book + ia)

```
Cost : 23

- The quiet oenophile adored rhododendrons but loathed zoological exoticness.

```
the
:elephant -:plane

quiet
:ticket -:tack + (:ocean - :cone + qu)

oenophile
:dolphin - :die + eoiee

adored
:teddybear - :bite - y + oi

rhododendrons
:rhinoceros - :ice + (:donkey - :key + (:diamond - :man - io + e))

but
:thumb - :home + eo

loathed
:elephant - :open + (:darts – star + oo)

zoological
:seagull - :sea + (:magic - :mug + (:snooze - :news + ow))

exoticness
:princess - :pear + (:teeth - :hot + (:mailbox - :lamb - i + ao)

```
Cost : 30

