# Schedule Demo

solve scheduling problem with a genetic algorithm

## branch better-ui

The setup of a new Problem shall be improved. The dialogue for that shall get 2
tabs:

### Tab "Problem"

<pre>
|-- table
|     |-- column "Priority": ascending ints
|     |-- column "Colors": displays a sample text with the specified foreground/background
|     |-- column "Tasks": displays all tasks of that priority
|
|-- buttons
|     |-- button "Add": starts dialogue "Priority details" for a new Priority entry
|     |-- button "Edit": starts same dialog for selected Priority entry
|     |-- button "Delete": deletes selected Priority entry (after further confirmation)
</pre>

Concerning the display of the tasks of a priority: there are 2 options:

1. further table in a table
2. Display in a sort of a text area with font _Courier New_ like this:

<pre>
| Task duration / s | Number of tasks |
|                10 |              20 |
|               100 |              30 |
|               500 |             120 |
</pre>

## Tab "Solution"

contains number entry fields for

* number of solutions
* #Workers/Solution
* #Slots/Worker
* Elitism Count
* Tournamen size
* Mutation rate (new)

## Dialogue "Priority Details"

<pre>
|-- Priority: readonly text field
|-- Colors:
|     |--Display: button with sample text, w/o functionality
|     |--Select Foreground: Button to start color selection dialogue
|     |--Select Background: Button to start color selection dialogue
|
|-- Tab "Tasks"
|     |--Table
|     |    | Column "Duration / s"
|     |    | Column "Number of tasks"
|     |
|     |--Buttons
|     |    |--Button "Add": adds new row at end of table
|     |    |--Button "Delete": deletes currently selected row (after confirmation)
</pre>

The fields in the table are editable