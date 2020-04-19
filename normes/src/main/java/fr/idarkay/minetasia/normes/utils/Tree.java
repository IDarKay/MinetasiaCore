package fr.idarkay.minetasia.normes.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author RedHeadEmile
 */
public class Tree
{
    private Branch mainBranch;

    public Tree()
    {
        this.mainBranch = new Branch(this, null, "main", Collections.emptyList());
    }

    public List<String> get(String args)
    {
        return this.get(args.toLowerCase().split(" "));
    }

    public List<String> get(String[] args)
    {
        if(args.length == 0) return mainBranch.getElements().stream().map(e -> e.getIdentifiers()).reduce(new ArrayList<>(), (sub, element) -> { sub.addAll(element); return sub; });

        List<Branch> b = new ArrayList<>();
        b.add(mainBranch);
        for(int i = 0; i < args.length; i++)
        {
            final int j = i;
            ArrayList<Branch> trusted = new ArrayList<>();
            b.forEach(br -> trusted.addAll(br.getElements().stream().filter(element -> element.getName().endsWith("%") || (j == args.length - 1 ? containsStartsWith(args[j].toLowerCase(), element.getIdentifiers()) : element.getIdentifiers().contains(args[j].toLowerCase()))).collect(Collectors.toList())));
            b = trusted;
        }
        ArrayList<String> trusted = new ArrayList<>();
        b.forEach(br -> trusted.addAll(br.getIdentifiers()));
        return trusted.stream().filter(str -> args.length != 0 ? str.toLowerCase().startsWith(args[args.length - 1]) : true).collect(Collectors.toList());
    }

    public Branch getBranch(String path)
    {
        String[] args = path.split("\\.");
        Branch b = mainBranch;
        for(int i = 0; i < args.length; i++)
        {
            final int j = i;
            b = b.getElements().stream().filter(branch -> branch.getName().equalsIgnoreCase(args[j])).findFirst().get();
        }
        return b;
    }

    private boolean containsStartsWith(String expression, List<String> elements)
    {
        for(String element : elements)
            if(element.toLowerCase().startsWith(expression))
                return true;
        return false;
    }

    public Branch addBranch(String name, List<String> identifiers)
    {
        return mainBranch.addBranch(name, identifiers);
    }

    public class Branch
    {
        private Tree tree;
        private Branch mainBranch;
        private String name;
        private List<String> identifiers;
        private List<Branch> elements;

        private Branch(Tree tree, Branch mainBranch, String name, List<String> identifiers)
        {
            this.tree = tree;
            this.mainBranch = mainBranch;
            this.name = name;
            this.elements = new ArrayList<>();
            this.identifiers = identifiers;
        }

        public Branch addBranch(String name, List<String> identifiers)
        {
            Branch branch = new Branch(tree, this, name, identifiers);
            elements.add(branch);
            return branch;
        }

        public Branch setIdentifiers(List<String> identifiers) { this.identifiers = identifiers; return this; }

        public Branch getMainBranch() { return this.mainBranch; }
        private List<Branch> getElements() { return this.elements; }

        public Tree getTree() { return this.tree; }
        public String getName() { return this.name; }
        public boolean isLeaf() { return false; }
        public List<String> getIdentifiers() { return this.identifiers; }
    }
}
