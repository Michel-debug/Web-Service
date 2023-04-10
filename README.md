# Web-Service
This is a project for class Web_Service, the topic is about lab members + semantic scholar

Member_1 : CHEN Junjie  michel724915929@gmail.com

Member_2 : FENG Jiaqi  jiaqifeng077@gmail.com

Introduction : 
This project aimed to realize the interaction between client and service, with the technology of SOAP and Restful. And it contains serveral different components are as follows.
1. Database ( Postgresql )
2. Service ( SOAP, RESTful )
3. Client  ( Tester toutes les fonctionnalités y compris externe web service )

Services' description : 
Our server provides functionality for managing labs and researchers through a web service. The main features of the server include:
1.Creating a researcher: Users can create a new researcher with a unique ID, name, email, lab name, and the number of articles they have published. The server stores this information in a PostgreSQL database.

2.Linking a lab with a researcher: The server allows users to associate a researcher with a specific lab by updating the lab name in the researcher's record in the database.

3.Deleting a researcher: Users can delete a researcher from the database by providing the researcher's ID.

4.Fetching researchers by lab: The server can retrieve a list of researchers associated with a specific lab by querying the database for all researchers with the provided lab name.

5.Creating a lab: Users can create a new lab with a unique name and a description. This information is stored in the database.

6.Deleting a lab: Users can delete a lab from the database by providing the lab's name. Before deleting the lab, the server unlinks all researchers associated with the lab.

7.Getting lab details by name: Users can retrieve a lab's details, such as its name and description, by providing the lab's name. If the lab is not found, the server returns a new empty Lab object with a "Not found" message.

The server's main method demonstrates the creation of a lab and a researcher, and how they can be linked together for test local.

------------------------------------------------------------------------------------------------------------------------------------------------------------------

Clients' descripion :

This Java client program is designed to interact with a Researcher Management System using both JAX-WS (Java API for XML Web Services) and JAX-RS (Java API for RESTful Web Services). It demonstrates various functionalities related to the management of researchers and their associated labs.

The main functionalities of the client program are as follows:

1.Creating labs and researchers in the local system using JAX-RS.

2.Retrieving labs by their name and researchers by their associated lab name.

3.Updating the lab of a researcher.

4.Deleting labs and researchers from the system.

5.Retrieving external researcher information using the Semantic Scholar API and storing it in the local system.

6.The program starts by testing JAX-WS functionalities for creating labs. Then, it demonstrates how to create labs and researchers in the local system using JAX-RS. It retrieves labs by their name and researchers by their associated lab name. It also updates a researcher's lab and deletes labs and researchers from the system.

Additionally, the program utilizes the Semantic Scholar API to fetch external researcher information based on researcher IDs. It stores the external researcher information, such as name, email, lab, and number of publications, into the local system. Finally, it cleans up the local system by deleting the researchers and labs fetched from the external API.

Overall, this client program demonstrates how to manage researchers and labs using JAX-WS and JAX-RS, as well as how to integrate external researcher data using the Semantic Scholar API.


----------------------------------------------------------------------------------------------------------------------------------------------------------------------

One use case demonstrating the functionality of our implementation : 

we'll demonstrate how the client program can be used to manage researchers and labs in a research organization.

First, the program creates two labs in the local system using JAX-RS: a "French Lab" and a "Chinese Lab".

Next, the program creates five researchers and associates them with the labs:

Researcher 1: Junjie CHEN (Chinese Lab)

Researcher 2: FENG Jiaqi (French Lab)

Researcher 3: WANG Jianguo (French Lab)

Researcher 4: ZHANG SAN (French Lab)

Researcher 5: LI Si (French Lab)

The program then retrieves the "French Lab" using its name and retrieves researchers associated with the "French Lab".

It updates the lab of Researcher 1 (Junjie CHEN) from "Chinese Lab" to "French Lab".

The program proceeds to delete both the "French Lab" and "Chinese Lab" from the system, as well as the five researchers.

The program uses the Semantic Scholar API to fetch external researcher information for several researcher IDs. It then stores this information, such as name, email, lab, and number of publications, into the local system.

Finally, the program cleans up the local system by deleting the researchers and labs fetched from the external API.

This example demonstrates how the client program can be effectively used to manage researchers and labs in a research organization. By using JAX-WS and JAX-RS to communicate with the local system, the program is able to create, retrieve, update, and delete researcher and lab information. Moreover, it can also integrate external researcher data from the Semantic Scholar API, providing a more comprehensive view of researchers and their work.

---------------------------------------------------------------------------------------------------------------------------------------------------------------------

