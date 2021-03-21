
# Summary: Plots simple and easy to read graph of
# tech skills
library(ggplot2) 

# This list does not relect my current knowledge or
# skill level, i.e. it's always considered out of date
df1 <- data.frame(
  "rest" = c("Rest APIs", "a", 3),
  "agile" = c("Agile", "a", 3),
  "ci" = c("CI", "a", 3),
  "microservices" = c("Micro-services", "a", 3),
  "testing" = c("Testing", "a", 2),
  "operations" = c("Operations", "a", 1),
  "java" = c("Java 11", "b", 4),
  "kt" = c("Kotlin 1.3", "b", 4),
  "c" = c("C", "b", 3),
  "lua" = c("Lua", "b", 2),
  "python" = c("Python", "b", 2),
  "android" = c("Android", "b", 3),
  "rust" = c("Rust", "b", 2),
  "go" = c("Go", "b", 2),
  "r" = c("R", "b", 1),
  "rshiny" = c("R-Shiny", "b", 1),
  "batch" = c("Batch", "b", 3),
  "bash" = c("Bash", "b", 3),
  "idea" = c("Intellij", "c", 4),
  "netbeans" = c("Netbeans", "c", 4),
  "atom" = c("Atom", "c", 4),
  "linux" = c("Linux (Ubuntu)", "c", 3),
  "windows" = c("Windows", "c", 2),
  "angular" = c("Angular", "d", 1),
  "json" = c("JSON", "d", 4),
  "openapi" = c("OpenAPI", "d", 3),
  "javascript" = c("JavaScript", "d", 3),
  "html" = c("HTML", "d", 3),
  "xml" = c("XML", "d", 2),
  "typescript" = c("TypeScript", "d", 2),
  "css" = c("CSS", "d", 2),
  "sqlite" = c("SQLite", "e", 3),
  "oracle_sql" = c("Oracle SQL", "e", 2),
  "git" = c("Git", "f", 3),
  "svn" = c("SVN", "f", 2),
  "gitlab" = c("GitLab", "f", 3),
  "github" = c("Github", "f", 2)
)

# Name rows for readability
row.names(df1)[1] = "tech_name"
row.names(df1)[2] = "tech_type"
row.names(df1)[3] = "competence"

# Transpose the data frame so columns -> rows and rows -> columns
print(df1)
df1 <- as.data.frame(t(df1))
print(df1)

# Competence was changed to a char in the above transpose
# Fix it by turning the value back to numeric
df1$competence <- as.numeric(as.character(df1$competence))

# Create a 4th column to hold the tech group as a number
# It will be used to group the technology type
df1$grouping <- as.numeric(df1$tech_type)

require(dplyr)
require(forcats)

# Plot a target diagram
# Diagram is first groupeed by 'tech_type' then ordered by 'competence'
p <- df1 %>% arrange(grouping, desc(-competence)) %>%
  mutate(tech_name = factor(tech_name, levels=tech_name)) %>%
  ggplot(
    aes(
      x=reorder(tech_name, -grouping),
      y=competence,
      fill=tech_type,
      colour="black"
    )
  )

p <- p + ylab("") + xlab("") + geom_bar(colour="black", stat="identity")


# Swap axis so X -> Y and Y -> X
p <- p + coord_flip()

# Remove legends etc & useless elements
p <- p + guides(fill=FALSE) + theme(legend.position="none")

# Change the Y axis numbers into meaningful text from the chart top
p <- p + scale_y_continuous(
  sec.axis = dup_axis(),
  labels=c("", "Beginner", "Growing", "Experienced", "Specialism")
)

# Styles the labels
p <- p + theme(
  axis.ticks=element_blank(),
  axis.text.x.bottom=element_blank(), 
  axis.text.y=element_text(
    size=15,
    face="bold",
    hjust="1",
    color="lightgrey"
  ),
  axis.text.x=element_text(
    angle=90,
    size=15,
    face="bold",
    hjust="0",
    color="lightgrey"
  ),
  panel.background = element_rect(fill = "transparent"),
  plot.background = element_rect(fill = "transparent")
)

# Output the resultant plot as a file
png(
  'tech-skills.png',
  width=300,
  height=800,
  units="px",
  bg = "transparent"
)
plot(p)
dev.off()
