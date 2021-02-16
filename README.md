# File-Type-Analyzer

Files come in all kind of possible type (formats): we usually see it specified in their 
names. Even if the file type was manual changed or even removed, information about the 
type is always contained within the file. So, in this project, we write a programme that 
extract this info to figure out the type of the file. 

The used approach to make this app is generaly used in many applications. Like, the 
_Unix “file”_ tool which relies on a sophisticated _magic_ database. Thus, we use different 
algorithms of _substring searching_ such as _naive_ approach, _KMP_, _Rabin-Karp_ and the 
Strategy pattern.

- The **_Knuth-Morris-Pratt_**(KMP) algorithm is an approach that allows solving the 
substring searching problem in linear time in the worst case. The algorithm compares 
a pattern with substrings of a text trying to find a complete matching. To decrease 
the number of comparisons, it uses the prefix function for finding an optimal pattern 
shift.

- **_Rabin-Karp_** algorithm is used for Substring searching. It uses string hashing 
(a technique for associating a string with a number) for a faster comparison thus 
significantly reducing the total running time compared with the naive approach.

- The _**Strategy pattern**_ : It represents algorithms (behavior) as classes with a common 
interface. It defines a family of algorithms and encapsulates each one in a separated 
class. It helps to avoid changes in existing classes when adding new strategies.

`</>`