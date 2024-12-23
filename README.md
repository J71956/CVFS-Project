Introduction:
This is a an in-memory Virtual File System (VFS), named the Comp VFS (CVFS), which is made in Java and utilizes OOP concepts to simulate a file system. The user opens application .java to start the program and use the file system. This is a group project made by Siu Yau Shing and Chow Kwan Ho.

The application uses the Model-View-Controller Pattern in Designing the CVFS. Simply run Application.Java and the program would start.
List of Commands
Creating a New Disk
Command: newDisk diskSize
Effect: Creates a new virtual disk with the specified maximum size. The previous working disk, if any, is closed. The newly created disk is set to be the working disk of the system, and the working directory is set to be the root directory of the disk.
Steps:
1.	Open the CLI tool.
2.	Type newDisk followed by the desired disk size (e.g., newDisk 1024).
3.	Press Enter.
Creating a New Document
Command: newDoc docName docType docContent
Effect: Creates a new document in the working directory with the specified name, type, and content.
Steps:
1.	Ensure you are in the desired directory.
2.	Type newDoc followed by the document name, type, and content (e.g., newDoc myfile txt "Hello World").
3.	Press Enter.
Creating a New Directory
Command: newDir dirName
Effect: Creates a new directory in the working directory with the specified name.
Steps:
1.	Ensure you are in the desired directory.
2.	Type newDir followed by the directory name (e.g., newDir myfolder).
3.	Press Enter.
Deleting a File
Command: delete fileName
Effect: Deletes an existing file with the specified name from the working directory.
Steps:
1.	Ensure you are in the directory containing the file.
2.	Type delete followed by the file name (e.g., delete myfile).
3.	Press Enter.
Renaming a File
Command: rename oldFileName newFileName
Effect: Renames an existing file in the working directory from oldFileName to newFileName.
Steps:
1.	Ensure you are in the directory containing the file.
2.	Type rename followed by the old file name and the new file name (e.g., rename myfile newfile).
3.	Press Enter.
Changing the Working Directory
Command: changeDir dirName
Effect: Changes the working directory to the specified directory name.
Steps:
1.	Type changeDir followed by the directory name (e.g., changeDir myfolder).
2.	If you want to move to the parent directory, type changeDir ...
3.	Press Enter.
Listing Files
Command: list
Effect: Lists all the files directly contained in the working directory.
Steps:
1.	Type list.
2.	Press Enter.
3.	View the list of files, including their names, types, and sizes.
Recursively Listing Files
Command: rList
Effect: Lists all the files contained in the working directory recursively.
Steps:
1.	Type rList.
2.	Press Enter.
3.	View the hierarchical list of files with indentation indicating the level of each file.
Constructing Simple Criteria
Command: newSimpleCri criName attrName op val
Effect: Constructs a simple criterion that can be referenced by criName.
Steps:
1.	Type newSimpleCri followed by the criterion name, attribute name, operator, and value (e.g., newSimpleCri cr1 name "contains” "report").
2.	Press Enter.
Checking if a File is a Document
Criterion name: IsDocument
Effect: Evaluates to true if and only if the file is a document.
Steps:
1.	Use this criterion in search commands to filter documents.
Constructing Composite Criteria
Command: newNegation criName1 criName2
Effect: Constructs a composite criterion that is the negation of an existing criterion.
Steps:
1.	Type newNegation followed by the new criterion name and the existing criterion name (e.g., newNegation cr2 cr1).
2.	Press Enter.
Command: newBinaryCri criName1 criName3 logicOp criName4
Effect: Constructs a composite criterion using a logical operator.
Steps:
1.	Type newBinaryCri followed by the new criterion name, first criterion, logical operator, and second criterion (e.g., newBinaryCri cr3 cr1 && cr2).
2.	Press Enter.
Printing All Defined Criteria
Command: printAllCriteria
Effect: Prints out all the defined criteria.
Steps:
1.	Type printAllCriteria.
2.	Press Enter.
3.	View the list of all defined criteria.
Searching for Files Based on a Criterion
Command: search criName
Effect: Lists all the files directly contained in the working directory that satisfy the specified criterion.
Steps:
1.	Type search followed by the criterion name (e.g., search cr1).
2.	Press Enter.
3.	View the list of files that match the criterion.
Recursively Searching for Files Based on a Criterion
Command: rSearch criName
Effect: Lists all the files contained in the working directory that satisfy the specified criterion recursively.
Steps:
1.	Type rSearch followed by the criterion name (e.g., rSearch cr1).
2.	Press Enter.
3.	View the hierarchical list of files that match the criterion.
Saving the Working Virtual Disk
Command: save path
Effect: Saves the working virtual disk into a file at the specified path.
Steps:
1.	Type save followed by the file path (e.g., save /path/to/diskfile).
2.	Press Enter.
Loading a Virtual Disk
Command: load path
Effect: Loads a virtual disk from a file at the specified path and makes it the working virtual disk.
Steps:
1.	Type load followed by the file path (e.g., load /path/to/diskfile).
2.	Press Enter.
Terminating the System
Command: quit
Effect: Terminates the execution of the system.
Steps:
1.	Type quit.
2.	Press Enter.
Undoing and Redoing Commands
Supported Commands: newDoc, newDir, delete, rename, changeDir, newSimpleCri, newNegation
Steps:
1.	To undo a command, type undo.
2.	Press Enter.
3.	To redo a command, type redo.
4.	Press Enter.
