#### Implementation of Karger's randomised algorithm for finding a minimum cut in a graph

The implemented algorithm outputs a minimum cut with an 
error probability of at most 0.01.

To run the algorithm write on the cmd line

`java RMinCut v1 v2 v2 v3 v5 v4   ...`

where each argument is an integer and the arguments are processed in pairs
to indicate the edges of the input graph: that is, the edges of the graph are {v1, v2},
{v2, v3}, {v5, v4} and so on.

More info on the algorithm: [https://en.wikipedia.org/wiki/Karger%27s_algorithm][https://en.wikipedia.org/wiki/Karger%27s_algorithm]

[https://en.wikipedia.org/wiki/Karger%27s_algorithm]: https://en.wikipedia.org/wiki/Karger%27s_algorithm