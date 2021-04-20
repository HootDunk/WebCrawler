# WebCrawler

A project for my Java class which tasked us with making a Class that, when given a starting wikipedia URL, traverses 1000 Wikipedia articles and counts the number of times that every word appears.  Additionally, the title of each page visited was to be stored and printed. 

I took advantage of the Jsoup library which provides an API for working with HTML data.  This simplified the text extraction process and provided very intuitive methods for manipulating the DOM much like one would in JavaScript. 

I used a Hash Map to store and count word occurrences  as it has an O(1) time efficiency for searching and inserting entries (the two most used operations in the code).  
