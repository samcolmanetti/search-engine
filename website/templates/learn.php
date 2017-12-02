<h1> So how does Soar work? </h1>
<h2>Major components</h2>
<ul>
  <li>Crawler</li>
  <li>Indexer</l1>
  <li>Searcher</l1>
</ul>

<h3>Crawler</h3>
<p>The crawler is responsible for going out on the web and following links it encounters. Those links get sent to the indexer for indexing. For more info see <span><a href="https://en.wikipedia.org/wiki/Web_crawler"</a>wikipedia.</a></span> </p>

<h3>Indexer</h3>
<p>The indexer is responsible for storing data it finds relevant on a web page to a database. The indexer usually stores data like the title, description, url, and terms with their frequencies. Soar does not store extremely common words such as "a", "an", or "the", since they are commonly found on every web page. These are called stop words. For more info see <span><a href="https://en.wikipedia.org/wiki/Search_engine_indexing"</a>wikipedia.</a></span> </p>

<h3>Searcher</h3>
<p>The searcher is responsible for querying the data stored in the database by the indexer. The searcher is also the component that ranks the results. For more info see <span><a href="https://en.wikipedia.org/wiki/Information_retrieval"</a>wikipedia.</a></span></p>

<h2>Ranking Algorithms</h2>
<p>There are various ranking algorithms available. Soar utilizes a few them. </p>

<h3>Boolean</h3>
<p>This is perhaps the most simple ranking algorithm available. A search with a boolean ranking system will return all the websites that contain the words in the query.</p>

<h3>Term frequency (tf)</h3>
<p>Generally speaking, pages that contain the words in the query more often, are generally more relevant than pages that contain those words less frequently. A search that is ranked by term frequency returns the web pages that contain the highest sum of the frequency of terms in the query.</p>

<h3>Term frequency - inverse document frequency (tf-idf)</h3>
<p>This ranking algorithm is similar to term frequency ranking, BUT it divides each term frequency by the number of times the term has appeared thoughout the entire document space (the internet). This makes the frequencies more <i>balanced</i>. If a word is extremely common, then the ranking system should pay little attention to that word, since it appears on plenty of other websites. However, if a webpage contains a word that is not commonly found, that web page should be ranked higher.</p>

<h3>Page Rank</h3>
<p>This ranking algorithm is considered the most effecient. It was created by Larry Page and Sergey Brin at Google. It is also a pun on Larry Page's name. This ranking algorithm essentially assigns a 'popularity' score to each web page. Pages that are more popular, meaning lots of other web pages contain links to them, have a high PageRank. If one of these 'popular' web pages contains a link to another web page, it is considered an endorsement and that page's PageRank score will increase because of it. This essentially envisions the web as a graph. </p>

<a href="/index.php">Take me back home</a>
