//!
//! # Key value store driver
//!
//! Drivers load and save key value pairs from and to other objects
//! or external media.
//!
//! TODO next: FileDriver
//!

use std::collections::HashMap;
use std::io;

pub type Map = HashMap<String, String>;
pub type IOResult = io::Result<HashMap<String, String>>;
pub type IOCheck = io::Result<()>;

/// Drivers that perform the actual reading and writing of key value
/// pairs to another media.
///
pub trait Driver {

    /// Loads key values pairs from external media clearing all
    /// current entries.
    ///
    /// Returns: Map of key value pairs
    ///
    fn load(&self) -> IOResult;

    /// Pushes the stores key value pairs to external media.
    ///
    /// * s: Map of key value pairs
    ///
    fn save(&mut self, s: &Map) -> IOCheck;
}

/// A driver that stores key value pairs within memory. Data is
/// lost when this driver is deallocated.
///
pub struct MemoryDriver {
    data: Map
}

/// Implements factory methods.
///
impl MemoryDriver {

    /// Creates a new memory driver.
    ///
    pub fn new() -> MemoryDriver {
        MemoryDriver { data: Map::new() }
    }

    /// Creates a new memory driver initialised with some key value pairs.
    ///
    /// * data: Key value pairs to initialise with
    ///
    pub fn from(data: Map) -> MemoryDriver {
        MemoryDriver { data }
    }
}

/// Implements driver trait for the memory driver.
///
impl Driver for MemoryDriver {

    /// Lends the data store.
    ///
    fn load(&self) -> IOResult {
        Ok(self.data.clone())
    }

    /// Sets the data as a clone of the supplied store
    ///
    /// * s: Key value pairs
    ///
    fn save(&mut self, s: &Map) -> IOCheck {
        self.data = s.clone();
        Ok(())
    }
}