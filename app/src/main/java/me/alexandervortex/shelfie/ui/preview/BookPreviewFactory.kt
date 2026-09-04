package me.alexandervortex.shelfie.ui.preview

import me.alexandervortex.shelfie.R

object BookPreviewFactory {

    fun getImages(): List<Int> {
        return listOf(
            R.drawable.img_4,
            R.drawable.img_5,
            R.drawable.img_1,
            R.drawable.img_2,
            R.drawable.img_3,
            R.drawable.img
        )
    }

    fun getTitles(): List<List<String>> {
        return listOf(
            listOf("1984", "George Orwell"),
            listOf("To Kill a Mockingbird", "Harper Lee"),
            listOf("The Great Gatsby", "F. Scott Fitzgerald"),
            listOf("Pride and Prejudice", "Jane Austen"),
            listOf("The Catcher in the Rye", "J.D. Salinger"),
            listOf("Moby-Dick", "Herman Melville"),
            listOf("War and Peace", "Leo Tolstoy"),
            listOf("Crime and Punishment", "Fyodor Dostoevsky"),
            listOf("The Lord of the Rings", "J.R.R. Tolkien"),
            listOf("Harry Potter and the Sorcerer's Stone", "J.K. Rowling"),
            listOf("The Hobbit", "J.R.R. Tolkien"),
            listOf("Brave New World", "Aldous Huxley"),
            listOf("The Chronicles of Narnia", "C.S. Lewis"),
            listOf("Jane Eyre", "Charlotte Brontë"),
            listOf("Wuthering Heights", "Emily Brontë"),
            listOf("Anna Karenina", "Leo Tolstoy"),
            listOf("The Picture of Dorian Gray", "Oscar Wilde"),
            listOf("Don Quixote", "Miguel de Cervantes"),
            listOf("The Divine Comedy", "Dante Alighieri"),
            listOf("The Alchemist", "Paulo Coelho"),
            listOf("The Little Prince", "Antoine de Saint-Exupéry"),
            listOf("The Kite Runner", "Khaled Hosseini"),
            listOf("One Hundred Years of Solitude", "Gabriel García Márquez"),
            listOf("Les Misérables", "Victor Hugo"),
            listOf("The Brothers Karamazov", "Fyodor Dostoevsky"),
            listOf("Fahrenheit 451", "Ray Bradbury"),
            listOf("The Old Man and the Sea", "Ernest Hemingway"),
            listOf("Dracula", "Bram Stoker"),
            listOf("Frankenstein", "Mary Shelley"),
            listOf("The Shining", "Stephen King")
        )
    }
}