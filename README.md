# animated-tribble

## RecyclerView

RecyclerView is designed to be efficient for displaying large lists or data that frequently changes. The list may contain items of different types.

LayoutManager determines how data in RecyclerView is displayed. Items may be in a list or grid layout, or even a custom layout.

RecyclerView does minimal work necessary by only loading items to be displayed on screen.

If items are modified, RecyclerView re-draws only the modified items, instead of the entire list.

RecyclerView recycles ViewHolders off-screen, for items that need to be displayed.

## Adapter

The list of items is provided to RecyclerView with the Adapter pattern, which converts one format to another format. In this case, instructions for using the data is provided to RecyclerView (providing implementation for abstract methods).

The information provided by a RecyclerView adapter is the number of items (to indicate how far to scroll), a method for binding a data item to a ViewHolder (displaying the item), and a method for creating ViewHolders.

## ViewHolder

ViewHolders contain views (which are grouped in a layout), along with metadata for the RecyclerView. By using a wrapper for views, the wrapper may be reused for different items. By recycling ViewHolders, ViewHolders do not have to be re-created or have layout re-inflated. Instead, the views contents are updated for the new item.

However, because the views are reused, properties set previously remain and may lead to unwanted behavior.

RecyclerView creates just enough ViewHolders visible on the screen, and binds data to the ViewHolder (setting content of ViewHolder based on data).

With separate ViewHolders, RecyclerView is able to update the binding on single ViewHolders, instead of updating all items.

## Updating data

RecyclerView uses DiffUtil, which uses Myer's diff algorithm to determines the minimal changes to convert one list to another.

## Binding Adapters

Binding adapters are used to set a property of a view based on logic. For example, setting the text property of a TextView.

Binding adapters use extension functions on the view, and the view in the layout references the logic. This leaves the responsibility of updating views to the view itself.

## Interacting with items (click listener)

Each ViewHolder contains reference to item and a click listener object. The click listener is passed the onclick method logic, which is defined in Fragment and passed to Adapter and then to ViewHolder.

## Dealing with differently typed items

Differently typed items extend a parent class (sealed class for restriction, as subclasses of sealed class must be declared in same file), so Adapter works with List<parentType>. The initial list is modified to map to parent type items and insert new types.

Different items may require different ViewHolders. RecyclerView differentiates item types with view types (integers) - items that require different ViewHolders have unique view types - and an items view type is determined by the getItemViewType method.

View types ensure only compatible ViewHolders are created/recycled for an item.

## Items of different span size (GridLayout)

In a GridLayout, each row contains n spans, specified in the GridLayoutManager constructor.

SpanSizeLookup class provides the number of spans each item occupies - the default implementation sets each item to occupy 1 span.

To set items to different span sizes, provide a SpanSizeLookup, and implement getSpanSize.

## Misc.

Sealed classes provide restricted set of types (similar to enums, as subclasses must be defined in same file), but allows freedom of representation of abstract classes.

`when` expressions are exhaustive (needs to cover all cases) and with sealed classes the compiler is able to verify this. Therefore, if a new class extends the sealed class, the compiler will indicate `when` expressions that need to be updated.

However, this works only if you use `when` as an expression (using the result) and not as a statement. For `when` statements, add an extension property
```
val <T> T.exhaustive: T
    get() = this
```
and now `when () {}.exhaustive` will cause compiler errors if all cases are not covered.

Data class is a class whose main purpose is to hold data.

The object keyword declares a singleton, and the instance has the same name as the class.

Companion object provides functions/properties tied to a class rather than instances (similar to static method)