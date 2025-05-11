# 1. Biological Inspiration and Natural Behavior

Manta rays are large marine animals known for their graceful and efficient foraging behaviors. In nature, several key behaviors drive their food-search strategy:

- **Chain Foraging:** Manta rays often forage in a chain-like formation. In this mode, individuals follow their peers in sequence toward promising feeding areas. This coordinated movement mimics a leader–follower dynamic where a well-informed individual (or group leader) guides the swarm to nutrient-rich zones.


- **Somersault Foraging:** Sometimes, manta rays perform dramatic flips or somersaults. This behavior is used to quickly change their position relative to the best-known feeding area, enabling them to cover a larger area and fine-tune their approach when prey is located nearby.


- **Cyclone Foraging:** In addition to chain and somersault behaviors, manta rays can also engage in a swirling or cyclone-like motion. This involves rapid circular movements around a target area, intensifying the local search and exploiting regions of the search space with high potential.

These natural behaviors inspire the algorithm’s mechanisms, imitating **exploration** (searching broadly with chain and cyclone foraging) and **exploitation** (local intensification via somersault foraging).

# 2. Algorithmic Mechanics

## Exploration vs. Exploitation

- **Exploration:**
The chain and cyclone foraging modes ensure that the algorithm can explore new regions of the search space. In chain foraging, individuals adjust their paths based on the global leader's position, ensuring a guided exploration. Cyclone foraging adds diversity by swirling around promising areas, potentially escaping local optima.


- **Exploitation:**
Somersault foraging is geared toward intensifying the search around the best solution discovered so far. When an individual performs a somersault, it essentially makes a large, targeted jump with a magnitude proportional to the gap between its current position and the best position. This accelerates convergence within promising regions.

## Initialization

- **Population and Parameter Initialization:**
The algorithm begins by initializing a population of candidate solutions (manta rays) with random positions within the defined search space. Key parameters include population size, number of iterations, learning rates, and foraging factors (which control the influence of chain, somersault, and cyclone foraging).

## Update Rules

- **Chain Foraging (Exploration):** Each individual updates its position by moving toward the best individual (the leader) in a chain-like manner.

  ![Chain Foraging Image](https://media.springernature.com/full/springer-static/image/art%3A10.1038%2Fs41598-024-59960-1/MediaObjects/41598_2024_59960_Fig2_HTML.png?as=webp)

- **Somersault Foraging (Exploitation):** To intensify the search, an individual makes a "somersault" motion.

  ![Somersault Foraging Image](https://media.springernature.com/lw685/springer-static/image/art%3A10.1038%2Fs41598-024-59960-1/MediaObjects/41598_2024_59960_Fig4_HTML.png?as=webp)

- **Cyclone Foraging (Intensification/Refinement):** Cyclone foraging introduces a swirling motion around the leader or a promising location.

  ![Cyclone Foraging Image](https://media.springernature.com/full/springer-static/image/art%3A10.1038%2Fs41598-024-59960-1/MediaObjects/41598_2024_59960_Fig3_HTML.png?as=webp)

# 3. Pseudocode

![Pseudocode](https://media.springernature.com/lw685/springer-static/image/art%3A10.1038%2Fs41598-024-59960-1/MediaObjects/41598_2024_59960_Figa_HTML.png)

# 4. Social Interaction

MRFO simulates a social communication model among individuals:

- **Leader–Follower Dynamics**: The best-performing solution in the population is designated as the leader. All individuals in the swarm update their positions by considering the leader’s position. This leader–follower scheme is central to the chain foraging mechanism.


- **Swarm Formation**: In cyclone foraging, the individuals "swirl" together around promising regions, mimicking natural cyclone behavior. This collective motion emphasizes local intensification through social interaction.


- **Influence Mechanisms**: The algorithm allows information sharing implicitly - when one individual improves its position (through somersault or any other mechanism), it becomes the new leader. This change rapidly influences the movement of the whole swarm, mimicking how group dynamics evolve in nature.

# 5. Mathematical Formulation and Parameter Overview

### Key Equations

- **Chain Foraging Equation:**  
  
  $$
  x^d_i(t+1) = \begin{cases} 
  x^d_i(t) + r \cdot \left( x^d_{best} (t) - x^d_i(t)\right) + \alpha \cdot \left( x^d_{best} (t) - x^d_i(t)\right), i=1 \\
  x^d_i(t) + r \cdot \left( x^d_{i-1} (t) - x^d_i(t)\right) + \alpha \cdot \left( x^d_{best} (t) - x^d_i(t)\right), i=2,3,...,N
  \end{cases}
  $$

  $$\alpha = 2 \cdot r \cdot \sqrt{|\log{r}|} $$

- **Somersault Foraging Equation:**

  $$
  x^d_i(t + 1) = x^d_i(t) + s \cdot \left( r_2 \cdot x^d_{best} - r_3 \cdot x^d_i(t)\right), i=1,2,...,N 
  $$

- **Cyclone Foraging Equation (Example Formulation)**:

  $$ 
  x^d_i(t+1)= \begin{cases}
  x^d_{best}(t) + r \cdot \left( x^d_{best}(t) - x^d_i(t) \right) + \beta \cdot \left( x^d_{best}(t) - i(t)\right), i = 1 \\
  x^d_{best}(t) + r \cdot \left( x^d_{i-1}(t) - x^d_i(t)\right) + \beta \cdot \left( x^d_{best}(t) - x^d_i(t)\right), i = 2,3,...,N
  \end{cases}
  $$

  $$ \beta = 2 \cdot e^{\frac{r_1(T-t+1)}{T}} \cdot \sin{(2\pi r_1)} $$

### Parameters

| Parameter                         | Meaning                                                                   |  
|-----------------------------------|---------------------------------------------------------------------------|  
| <b> Population Size $N$ </b>      | Number of manta rays (candidate solutions)                                |  
| <b> Dimensions $d$ </b>           | Number of variables in the optimization problem                           |
| <b> $ r $ </b>                    | Random number $r\in(0,1)$                                                 |  
| <b> $ s $ </b>                    | Somersault factor controlling the leap toward the best position                                      |  
| <b> $ \alpha $ </b>               | weight coefficient                                                        |
| <b> $ \beta $ </b>                | Inertia weight                                                            |
| <b> $ x^d_i(t) $ </b>             | the current position of the $d^{th}$ dimension of the $i^{th}$ idividual  |
| <b> $ x^d_{best}(t) $ </b>        | the best position at $t^{th}$ iteration of the current $d^{th}$ dimension |
| <b> $ t $ </b> and <b> $ T $ </b> | the current and maximum number of iterations                                                                         |

# References

1. [W. Zhao, Z. Zhang, and L. Wang, “Manta ray foraging optimization: An effective bio-inspired optimizer for engineering applications,” Engineering Applications of Artificial Intelligence, vol. 87, p. 103300, Jan. 2020, doi: 10.1016/j.engappai.2019.103300. ](https://doi.org/10.1016/j.engappai.2019.103300)
2. [P. Qu, Q. Yuan, F. Du, and Q. Gao, “An improved manta ray foraging optimization algorithm,” Sci Rep, vol. 14, no. 1, May 2024, doi: 10.1038/s41598-024-59960-1. ](https://doi.org/10.1038/s41598-024-59960-1)