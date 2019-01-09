# Search Engine
## Context
There are over one billion accessible websites on the world-wide web. The size of internet grows
each day as more people and businesses register new domains and fill them with web pages. A
search engine crawls through the entire accessible web and indexes each page for information. The
page’s information and metadata will be stored in a database. This allows users to query the internet
to quickly find the website they are seeking. I intend to build my own search engine. This search
engine will have a convenient web interface for users to perform queries. This is also intended to be
an educational tool for students. Search engines can get extremely complex, but basic search engines
can be built with a reasonable amount of time and resources. I want to build an open sourced search
engine that is simple for students to understand and modify. Students should have a better
understanding of how search engines work “under the hood” after performing queries. I want users
to be able to choose how the pages will be ranked (boolean, term frequency, term frequency –
inverse document frequency, page rank) for educational and testing purposes. This will help students
understand different ideas in information retrieval. Statistics such as number of pages indexed,
number of websites found, and processing time should be returned to the user. There will also be a
webpage that explains how the results were ranked and displays the computational steps that lead to
the overall results. In terms of hardware, I will need a machine with enough storage to store a
considerable number of webpages. My initial goal is to index about 500 million webpages.

Who need it
• Those who are curious about how search engines work

Reasons for the new system
• Want to learn more about how search engines operate under the hood
• Want to help students learn more about search engines
• An open sourced search engine focused on students does not exist

Where will it be used
• Will be used by researchers and students

Hardware/software required
• High end server – 32GB RAM and 4-6TB of persistent storage
• HackLang (main backend code and API)
• Java (some backend code)
• Postgres SQL
• Sketch (for designing)

Intended user group
• Students
• Researchers

## Overview
### Description of Problem
#### Building a search engine
There are several functions that a search engine must do. Mainly those functions are crawling,
indexing, and retrieving. The web crawler will go on the web and download webpages to be indexed.
The indexer will figure out what information is relevant and store it in a database. We then apply
information retrieval techniques to find and rank relevant webpages based on a user’s query. The
problem is that crawling the web and figuring out which information is needed is difficult and
requires lots of time and storage. It’s difficult to make it efficient. It is even more difficult to rank
the webpages. There are lots of ways to approach it. I plan to start off with a simple Boolean search
and work my way up to PageRank.

#### Educational Tool
Search Engines appear to most students as a magical “black box” that just works. Most students do
not have a good understanding on how queries are processed and results are obtained. That is
because users use a search engine like Google where the results are returned, but users have no idea
how those results were obtained. This search engine will give users the options to see how the query
was evaluated and how the pages were ranked.
Objectives
• Build a web interface capable of interpreting queries
• Build a search engine capable of processing queries
• Build a crawler that can access webpages
• Build an indexer that can efficiently store relevant content from webpages into a database
• Build a retrieval system capable of finding content and ranking the results
• Build a page that could effectively explain some of the technical details on how the results were obtained
• Encourage users/students to learn more about how search engines work by using the built in educational tools

### Inputs of System
* World Wide Web
* Query
* Ranking method (optional – used in educational tool)

The web crawler will go on the World Wide Web and find webpages to be indexed. The
webpages will be in HTML format. I will start off with a pool of URLs that need to be crawled.
The crawler will send HTTP GET requests to each of those URLs, acquire the webpage, and
filter out URLs from each page. Each of those URLs will then get added back into the pool. A
database table will store when each page was crawled. Pages will only be crawled again after a
certain amount of time has passed and if the data has changed.
Queries will be provided by users using the web interface. The format will be standard ASCII text.

### Outputs of System
The outputs of the system will be a list of links corresponding to users’ queries ranked by relevance.
The outputs of the education tool will focus on informing the user how the results were obtained to
help better understand how a search engine works.

Educational tool outputs
* Table showing websites containing terms in query, term frequencies, term frequency-inverse document frequency
* Related terms that were also searched
  * e.g. fast -> quick, cat -> kitten, chair -> stool
* Processing time
* Number of pages found with key words
* PageRank

## Constraints
* Learning curve for Hack (programming language)
* Limited computing power
* Limited amount of time to gather and analyze data
* Limited amount of storage (4TB)

## Feasibility
Given that I only have one semester to work on this project, I will not be able to complete all the
features listed. A basic search engine with a term-frequency inverse document frequency ranking
algorithm will be completed. PageRank may be a little out of reach, but some progress will be made.
The number of webpages indexed will depend on how much time and computing resources I have
access to.
