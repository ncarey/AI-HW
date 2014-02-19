DEFAULT_TARGET = hw1
ALL_TARGETS = hw1 hw2

EXTENSION = pdf
LATEX = pdflatex
VIEWER = xpdf

OUTDIR = ./output

default: tex-simple


tex-simple:
	@ $(LATEX) -output-directory $(OUTDIR) -file-line-error -interaction nonstopmode $(DEFAULT_TARGET).tex 

#NOTE: here we do things enough times to get all bibliographic and intra-document references set up right; the first pass builds the document and the second puts in the references.
tex-full:
	$(LATEX) $(DEFAULT_TARGET).tex; \
	bibtex $(DEFAULT_TARGET); \
	$(LATEX) $(DEFAULT_TARGET).tex; \
	$(LATEX) $(DEFAULT_TARGET).tex; \
	for i in log aux blg bbl $(EXTENSION); \
		do mv $(DEFAULT_TARGET).$$i $(OUTDIR); done


#NOTE: this is a generic rule do `make foo` to have this rule used to build foo.tex
%: %.tex
	$(LATEX) -output-directory $(OUTDIR) -file-line-error -interaction nonstopmode $@.tex

		
view:
	$(VIEWER) $(OUTDIR)/$(DEFAULT_TARGET).$(EXTENSION) &


ps:
	pdf2ps -o $(OUTDIR)/$(DEFAULT_TARGET).ps $(OUTDIR)/$(DEFAULT_TARGET).pdf &


all:
	for i in $(ALL_TARGETS); do $(LATEX) $$i.tex; bibtex $$i; $(LATEX) $$i.tex; $(LATEX) $$i.tex; dvips -o $$i.ps $$i.dvi; dvipdf $$i.dvi; done; mv *dvi *ps *pdf $(OUTDIR)


clean:
	rm *.log *.blg *.dvi *.aux *.ppl *~ $(OUTDIR)/*.log $(OUTDIR)/*.bib $(OUTDIR)/*.aux $(OUTDIR)/*.bbl $(OUTDIR)/*.blg $(OUTDIR)/*.dvi ; mv *.ps *.pdf $(OUTDIR)
